package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetTitleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a){
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        if (a.length < 1){ p.sendMessage("§e/settitle <titleId>"); return true; }
        TalUltimatePlugin.TITLES.setCurrent(p, a[0]);
        return true;
    }
}
