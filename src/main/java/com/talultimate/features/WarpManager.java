package com.talultimate.features;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpManager {
    private final JavaPlugin plugin;

    public WarpManager(JavaPlugin plugin) { this.plugin = plugin; }

    public void warp(Player p, String key) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("warps." + key);
        if (sec == null) { p.sendMessage("§e워프가 설정되어 있지 않습니다: " + key); return; }
        String world = sec.getString("world","world");
        double x = sec.getDouble("x"), y = sec.getDouble("y"), z = sec.getDouble("z");
        float yaw = (float) sec.getDouble("yaw", 0), pitch = (float) sec.getDouble("pitch", 0);
        var w = plugin.getServer().getWorld(world);
        if (w == null) { p.sendMessage("§c월드를 찾을 수 없습니다: " + world); return; }
        p.teleport(new Location(w, x, y, z, yaw, pitch));
        p.sendMessage("§a워프로 이동했습니다: " + key);
    }
}

