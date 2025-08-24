package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAdminCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin; private final boolean add;
    public TitleAdminCommand(TalUltimatePlugin plugin, boolean add){ this.plugin=plugin; this.add=add; }

    @Override public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a){
        if (!s.hasPermission("tal.admin")){ s.sendMessage("§c권한 없음"); return true; }
        if (a.length<2){ s.sendMessage(add? "§e/addtitle <player> <titleId>":"§e/removetitle <player> <titleId>"); return true; }
        Player t = Bukkit.getPlayer(a[0]);
        if (t==null){ s.sendMessage("§c플레이어가 오프라인"); return true; }
        if (add) TalUltimatePlugin.TITLES.grant(t, a[1], true);
        else s.sendMessage("§7(간단 버전: 제거 생략 — 추후 확장)");
        return true;
    }
}
class SetTitleCommand implements CommandExecutor {
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if (!(s instanceof org.bukkit.entity.Player p)){ s.sendMessage("player only"); return true; }
        if (a.length<1){ s.sendMessage("§e/settitle <titleId>"); return true; }
        TalUltimatePlugin.TITLES.setCurrent(p, a[0]);
        return true;
    }
}
