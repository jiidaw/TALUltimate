package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.configuration.ConfigurationSection;

public class AdvancementListener implements Listener {
    @EventHandler public void onDone(PlayerAdvancementDoneEvent e){
        String key = e.getAdvancement().getKey().toString();
        ConfigurationSection list = TalUltimatePlugin.getPlugin(TalUltimatePlugin.class).getConfig().getConfigurationSection("titles.list");
        if (list==null) return;
        for (String id : list.getKeys(false)){
            ConfigurationSection s = list.getConfigurationSection(id+".obtained-by");
            if (s==null) continue;
            if ("ADVANCEMENT".equalsIgnoreCase(s.getString("type")) && key.equalsIgnoreCase(s.getString("key"))){
                TalUltimatePlugin.TITLES.grant(e.getPlayer(), id, false);
            }
        }
    }
}
