package com.talultimate.features;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class UiMenuCommand implements CommandExecutor, Listener {
    private final JavaPlugin plugin;
    public UiMenuCommand(JavaPlugin plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // 여기에 메뉴 열기 GUI 로직(인벤토리) 넣으면 됨
        p.sendMessage("§a메뉴가 아직 간단 버전입니다. (/sell all, /gachaitem, /gachatitle, /settitle 등 사용)");
        return true;
    }
}
