package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitListener implements Listener {
    public JoinQuitListener(TalUltimatePlugin plugin){ }
    @EventHandler public void onJoin(PlayerJoinEvent e){
        if (!TalUltimatePlugin.TITLES.has(e.getPlayer(), "newbie")){
            TalUltimatePlugin.TITLES.grant(e.getPlayer(), "newbie", false);
        }
        e.getPlayer().sendMessage("§e환영합니다! §f칭호는 /settitle 로 변경 가능.");
    }
}
