package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.features.TitleManager.Grade;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    private final TalUltimatePlugin plugin;
    public AdvancementListener(TalUltimatePlugin plugin){ this.plugin = plugin; }

    @EventHandler
    public void onAdv(PlayerAdvancementDoneEvent e){
        var adv = e.getAdvancement();
        NamespacedKey key = adv.getKey();
        if (!"minecraft".equals(key.getNamespace())) return;

        // TitleManager가 생성해 둔 id 규칙과 동일하게 구성
        String id = "adv_" + key.getKey().replace('/', '_');
        var tm = TalUltimatePlugin.TITLES;

        // 칭호 메타가 없으면(동적 생성이 안 되었다면) 무시
        var meta = tm.getMeta(id);
        if (meta == null) return;

        // 자동 지급은 최대 UNIQUE까지만
        if (meta.grade == Grade.LEGEND || meta.grade == Grade.DUSK) return;

        // 지급
        tm.grant(e.getPlayer(), id, false);
        e.getPlayer().sendMessage("§a도전과제 달성! 칭호 획득: " + meta.display);
    }
}
