package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {
    private final TalUltimatePlugin plugin;
    private final Map<String, Long> lastUse = new HashMap<>();
    public WarpManager(TalUltimatePlugin plugin){ this.plugin = plugin; }

    public boolean warp(Player p, String key){
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("warps."+key);
        if (sec==null){ p.sendMessage("§c워프 미설정: "+key); return false; }
        long cd = sec.getLong("cooldown_seconds", 0);
        String lk = p.getUniqueId()+":"+key;
        long now = System.currentTimeMillis();
        if (cd>0){
            long last = lastUse.getOrDefault(lk,0L);
            long remain = (last + cd*1000L) - now;
            if (remain>0){ p.sendMessage("§e쿨타임 "+(remain/1000)+"초 남음"); return false; }
            lastUse.put(lk, now);
        }
        World w = Bukkit.getWorld(sec.getString("world","world"));
        if (w == null){ p.sendMessage("§c월드를 찾을 수 없습니다."); return false; }
        double x = sec.getDouble("x",0), y = sec.getDouble("y",64), z = sec.getDouble("z",0);
        if (sec.getBoolean("safe_y", true)){
            int hy = w.getHighestBlockYAt((int)Math.floor(x),(int)Math.floor(z));
            if (hy>0) y = hy + 1.0;
        }
        float yaw = (float) sec.getDouble("yaw", 0), pitch=(float)sec.getDouble("pitch",0);
        p.teleport(new Location(w, x+0.5, y, z+0.5, yaw, pitch));
        p.sendMessage("§b이동: " + key);
        return true;
    }
}
class WarpCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin; private final String key; private final String perm;
    public WarpCommand(TalUltimatePlugin plugin, String key, String perm){ this.plugin=plugin; this.key=key; this.perm=perm; }
    @Override public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a){
        if (!(s instanceof Player p)) { s.sendMessage("player only"); return true; }
        if (perm!=null && !p.hasPermission(perm)){ p.sendMessage("§c권한 없음"); return true; }
        TalUltimatePlugin.WARPS.warp(p, key); return true;
    }
}
