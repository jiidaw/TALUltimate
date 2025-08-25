package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelRewardListener implements Listener {
    @EventHandler public void onLevel(PlayerLevelChangeEvent e){
        if (e.getNewLevel() > e.getOldLevel()){
            int newLv = e.getNewLevel();
            long reward = (long) newLv * TalUltimatePlugin.ECON.levelReward;
            TalUltimatePlugin.ECON.add(e.getPlayer(), reward);
            e.getPlayer().sendMessage("§b레벨업! §d"+newLv+" §7→ 보상 §a"+ Util.money(reward));
        }
    }
}
