package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelRewardListener implements Listener {

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent e){
        Player p = e.getPlayer();
        int newLv = e.getNewLevel();
        if (newLv <= e.getOldLevel()) return;

        int per = TalUltimatePlugin.getPlugin(TalUltimatePlugin.class)
                .getConfig().getInt("economy.level-reward-per-level", 2000);
        long reward = (long)newLv * per;

        TalUltimatePlugin.ECON.give(p, reward);
        p.sendMessage("§a레벨 " + newLv + " 달성! 보상 " + reward + "원 지급");
    }
}
