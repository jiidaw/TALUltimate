package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * - 보유 칭호/현재 칭호 관리
 * - 발전과제 자동 칭호 생성/지급(최대 UNIQUE)
 * - 등급별 색/혜택 적용 (돈 배율, 판매 보너스, 스피드)
 */
public class TitleManager implements Listener {

    public enum Grade { NEWBIE, COMMON, UNIQUE, LEGEND, DUSK }

    private final TalUltimatePlugin plugin;

    // 플레이어별: 보유 칭호 IDs, 현재 칭호 ID
    private final Map<UUID, Set<String>> owned = new ConcurrentHashMap<>();
    private final Map<UUID, String> current = new ConcurrentHashMap<>();

    // 캐시: id -> TitleMeta
    private final Map<String, TitleMeta> titles = new HashMap<>();

    public TitleManager(TalUltimatePlugin plugin){
        this.plugin = plugin;
        loadFromConfig();
        // 조인 시 뉴비/표시/혜택 적용
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /* ---------- 데이터 모델 ---------- */
    public static class TitleMeta {
        public final String id;
        public final String display;
        public final Grade grade;
        public final String color; // '§' or hex
        public final String obtainType; // JOIN / ADVANCEMENT / ACTION / GACHA
        public final String advId;      // ADVANCEMENT일 경우
        public final boolean broadcast; // 황혼 이상 방송 여부
        // perks
        public final double moneyMultiplier; // 돈 지급 배율 (1.00 = 기본)
        public final double sellBonus;       // 판매 보너스 (+0.05 = +5%)
        public final double speed;           // 이동속도 가산 (0.02 = +2%)

        public TitleMeta(String id, String display, Grade grade, String color,
                         String obtainType, String advId, boolean broadcast,
                         double moneyMultiplier, double sellBonus, double speed) {
            this.id = id; this.display = display; this.grade = grade; this.color = color;
            this.obtainType = obtainType; this.advId = advId; this.broadcast = broadcast;
            this.moneyMultiplier = moneyMultiplier; this.sellBonus = sellBonus; this.speed = speed;
        }
    }

    /* ---------- 로딩: config + 모든 발전과제 자동 생성 ---------- */
    private void loadFromConfig(){
        titles.clear();

        // 기본 등급 색 (grades)
        ConfigurationSection grades = plugin.getConfig().getConfigurationSection("titles.grades");

        // 명시된 리스트 먼저 로딩
        ConfigurationSection list = plugin.getConfig().getConfigurationSection("titles.list");
        if (list != null){
            for (String id : list.getKeys(false)){
                ConfigurationSection s = list.getConfigurationSection(id);
                String display = s.getString("display", id);
                String gradeStr = s.getString("grade", "COMMON");
                Grade grade = Grade.valueOf(gradeStr.toUpperCase(Locale.ROOT));
                String color = s.getString("color", grades != null ? grades.getString(grade.name(), "§f") : "§f");

                ConfigurationSection ob = s.getConfigurationSection("obtained-by");
                String type = (ob != null ? ob.getString("type", "GACHA") : "GACHA").toUpperCase(Locale.ROOT);
                String adv = (ob != null ? ob.getString("id", null) : null);
                boolean broadcast = s.getBoolean("broadcast", grade == Grade.DUSK);

                ConfigurationSection perks = s.getConfigurationSection("perks");
                double mm = perks!=null ? perks.getDouble("money-multiplier", 1.00) : 1.00;
                double sb = perks!=null ? perks.getDouble("sell-bonus", 0.00) : 0.00;
                double sp = perks!=null ? perks.getDouble("speed", 0.00) : 0.00;

                titles.put(id, new TitleMeta(id, display, grade, color, type, adv, broadcast, mm, sb, sp));
            }
        }

        // ★ 모든 발전과제 → 자동 칭호 생성 (존재하지 않는 경우만)
        //    - 표시 이름은 Advancement Display title 사용 (없으면 key fallback)
        //    - 등급 배정 규칙(대략): story/* = COMMON, end/kill_dragon 등 핵심 = UNIQUE
        //    - LEGEND/DUSK는 자동 생성하지 않음(가챠 전용 정책 유지)
        Iterator<org.bukkit.advancement.Advancement> it = Bukkit.advancementIterator();
        while (it.hasNext()){
            var adv = it.next();
            NamespacedKey key = adv.getKey();
            if (!"minecraft".equals(key.getNamespace())) continue; // 기본만

            String id = "adv_" + key.getKey().replace('/', '_'); // 예: story/mine_stone -> adv_story_mine_stone
            if (titles.containsKey(id)) continue;

            // 타이틀(표시 문자열) 가져오기
            String displayName = key.getKey(); // fallback
            try {
                var disp = adv.getDisplay();
                if (disp != null && disp.title() != null) displayName = disp.title().toString();
            } catch (Throwable ignored){}

            // 난이도/범주에 따른 대략 등급 산정(최대 UNIQUE까지만)
            Grade g = guessGradeFromKey(key.getKey()); // COMMON/UNIQUE 중 하나
            String color = grades != null ? grades.getString(g.name(), "§f") : (g==Grade.UNIQUE ? "§a" : "§f");

            titles.put(id, new TitleMeta(
                    id, displayName, g, color,
                    "ADVANCEMENT", "minecraft:"+key.getKey(), false,
                    1.00, 0.00, 0.00
            ));
        }
    }

    private Grade guessGradeFromKey(String path){
        // 아주 거친 분류: 엔드/네더/보스/귀중 광물 관련은 UNIQUE, 그 외 COMMON
        String p = path.toLowerCase(Locale.ROOT);
        if (p.contains("end/") || p.contains("nether/") || p.contains("kill_dragon")
                || p.contains("summon_wither") || p.contains("adventure/")) {
            return Grade.UNIQUE;
        }
        return Grade.COMMON;
    }

    /* ---------- 퍼블릭 API ---------- */

    public void grant(Player p, String id, boolean announce){
        TitleMeta meta = titles.get(id);
        if (meta == null) return;
        owned.computeIfAbsent(p.getUniqueId(), k -> ConcurrentHashMap.newKeySet()).add(id);

        // 방송 규칙
        if (announce && (meta.grade == Grade.DUSK || meta.broadcast)) {
            Bukkit.broadcastMessage("§d"+p.getName()+"님이 ["+meta.display+"] 칭호를 획득했습니다!");
        } else {
            p.sendMessage("§b칭호 획득: " + meta.display);
        }
    }

    public void setCurrent(Player p, String id){
        // 반드시 보유해야 설정 가능
        if (!owned.getOrDefault(p.getUniqueId(), Set.of()).contains(id)){
            p.sendMessage("§e해당 칭호를 보유하고 있지 않습니다.");
            return;
        }
        current.put(p.getUniqueId(), id);
        p.sendMessage("§a칭호가 변경되었습니다: " + id);
        applyPerks(p);
    }

    public String getCurrent(Player p){
        return current.getOrDefault(p.getUniqueId(), "newbie");
    }

    public TitleMeta getMeta(String id){ return titles.get(id); }

    public Set<String> getOwned(Player p){
        return owned.getOrDefault(p.getUniqueId(), Collections.emptySet());
    }

    /* ---------- 혜택 적용 ---------- */
    public void applyPerks(Player p){
        // 기존 스피드 제거
        p.removePotionEffect(PotionEffectType.SPEED);

        TitleMeta m = titles.get(getCurrent(p));
        if (m == null) return;

        // 이동속도 (포션으로 간단 적용)
        if (m.speed > 0.0001){
            // 속도 2% => 증폭 0 유지, 지속 10분
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*600, 0, true, false, false));
        }

        // 돈/판매 보너스는 EconomyManager가 계산 시점에 이 값을 읽어 사용
    }

    /* ---------- 조인 시 기본 처리 ---------- */
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        owned.computeIfAbsent(p.getUniqueId(), k -> ConcurrentHashMap.newKeySet());
        if (!owned.get(p.getUniqueId()).contains("newbie")){
            owned.get(p.getUniqueId()).add("newbie");
            current.put(p.getUniqueId(), "newbie");
        }
        applyPerks(p);
    }
}
