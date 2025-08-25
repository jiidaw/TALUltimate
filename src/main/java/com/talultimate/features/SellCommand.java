package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SellCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin;
    public SellCommand(TalUltimatePlugin plugin){ this.plugin = plugin; }

    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a){
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        boolean all = a.length>0 && a[0].equalsIgnoreCase("all");
        TalUltimatePlugin.ECON.sell(p, all);
        return true;
    }
}
