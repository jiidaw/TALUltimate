package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.*;

public class TitleManager {
    private final Map<String, TitleDef> defs = new LinkedHashMap<>();
    private final Map<UUID, String> current = new HashMap<>();
    private final Map<UUID, Set<String>> owned = new HashMap<>();
    private final Map<String, String> gradeColor = new HashMap<>();

    public TitleManager(TalUltimatePlugin plugin){
        ConfigurationSection colors = plugin.getConfig().getConfigurationSection("titles.colors");
        if (colors!=null) for (String k: colors.getKeys(false)) gradeColor.put(k, colors.getString(k));
        ConfigurationSection list = plugin.getConfig().getConfigurationSection("titles.list");
        if (list!=null) for (String id: list.getKeys(false)){
            ConfigurationSection s = list.getConfigurationSection(id);
            TitleDef d = new TitleDef();
            d.id=id;
            d.name = s.getString("name", id);
            d.grade = s.getString("grade", "COMMON");
            d.color = Util.color(s.getString("color", gradeColor.getOrDefault(d.grade, "§f")));
            ConfigurationSection ob = s.getConfigurationSection("obtained-by");
            if (ob!=null){
                d.obtainType = ob.getString("type","GACHA");
                d.obtainKey = ob.getString("key","");
                d.action = ob.getString("action","");
                d.count = ob.getInt("count",1);
            }
            d.bonusXp = s.getInt("benefits.xp-bonus", 0);
            d.bonusSell = s.getInt("benefits.sell-bonus", 0);
            defs.put(id, d);
        }
    }
    public String getCurrentTitleName(Player p){
        String id = current.getOrDefault(p.getUniqueId(), "newbie");
        TitleDef d = defs.get(id); if (d==null) return "없음";
        return d.color + d.name;
    }
    public void grant(Player p, String id, boolean broadcastLegendOrHigher){
        TitleDef d = defs.get(id); if (d==null) return;
        owned.computeIfAbsent(p.getUniqueId(), k->new HashSet<>()).add(id);
        p.sendMessage("§a칭호 획득: " + d.color + d.name);
        if (broadcastLegendOrHigher && (d.grade.equalsIgnoreCase("LEGEND") || d.grade.equalsIgnoreCase("TWILIGHT"))){
            p.getServer().broadcastMessage("§6[칭호] §e"+p.getName()+"§f 님이 §l"+ d.name +"§r§f 칭호를 획득했습니다!");
        }
    }
    public boolean setCurrent(Player p, String id){
        if (!owned.getOrDefault(p.getUniqueId(), Set.of()).contains(id)){ p.sendMessage("§e해당 칭호를 갖고 있지 않습니다."); return false; }
        current.put(p.getUniqueId(), id);
        p.sendMessage("§b현재 칭호 변경: " + defs.get(id).color + defs.get(id).name);
        return true;
    }
    public boolean has(Player p, String id){ return owned.getOrDefault(p.getUniqueId(), Set.of()).contains(id); }
    public static class TitleDef {
        public String id, name, grade, color, obtainType, obtainKey, action;
        public int count, bonusXp, bonusSell;
    }
}
