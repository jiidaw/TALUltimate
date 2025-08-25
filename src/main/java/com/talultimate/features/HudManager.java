package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class HudManager {
    private int taskId = -1;
    public HudManager(TalUltimatePlugin plugin){ }
    public void start(){
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("TALUltimate"), () -> {
            ScoreboardManager sm = Bukkit.getScoreboardManager();
            if (sm==null) return;
            for (Player p : Bukkit.getOnlinePlayers()){
                Scoreboard sb = sm.getNewScoreboard();
                Objective o = sb.registerNewObjective("talhud","dummy","§b§lTAL §7HUD");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                String title = TalUltimatePlugin.TITLES.getCurrentTitleName(p);
                long money = TalUltimatePlugin.ECON.get(p);
                o.getScore("§f플레이어: §a"+p.getName()).setScore(4);
                o.getScore("§f칭호: §e"+title).setScore(3);
                o.getScore("§f잔액: §b"+ Util.money(money)).setScore(2);
                o.getScore("§f레벨: §d"+p.getLevel()).setScore(1);
                p.setScoreboard(sb);
            }
        }, 20L, 60L);
    }
    public void stop(){
        if (taskId!=-1) Bukkit.getScheduler().cancelTask(taskId);
    }
}
