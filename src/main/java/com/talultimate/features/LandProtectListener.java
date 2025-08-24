package com.talultimate.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class LandProtectListener implements Listener {
    @EventHandler public void onBreak(BlockBreakEvent e){
        var w = e.getBlock().getWorld().getName();
        var x = e.getBlock().getX(); var z = e.getBlock().getZ();
        boolean ok = com.talultimate.TalUltimatePlugin.LAND.isInside(w,x,z, e.getPlayer().getUniqueId());
        if (!ok){ e.setCancelled(true); e.getPlayer().sendMessage("§c남의 땅입니다!"); }
    }
    @EventHandler public void onPlace(BlockPlaceEvent e){
        var w = e.getBlock().getWorld().getName();
        var x = e.getBlock().getX(); var z = e.getBlock().getZ();
        boolean ok = com.talultimate.TalUltimatePlugin.LAND.isInside(w,x,z, e.getPlayer().getUniqueId());
        if (!ok){ e.setCancelled(true); e.getPlayer().sendMessage("§c남의 땅입니다!"); }
    }
}
