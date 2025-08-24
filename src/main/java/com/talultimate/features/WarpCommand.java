package com.talultimate.features;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements CommandExecutor {
    private final String key;
    private final String perm;

    public WarpCommand(com.talultimate.TalUltimatePlugin plugin, String key, String perm){
        this.key = key;
        this.perm = perm;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a){
        if (!(s instanceof Player p)) { s.sendMessage("player only"); return true; }
        if (perm != null && !p.hasPermission(perm)){ p.sendMessage("§c권한 없음"); return true; }
        com.talultimate.TalUltimatePlugin.WARPS.warp(p, key);
        return true;
    }
}
