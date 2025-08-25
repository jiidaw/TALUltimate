package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

public class HudManager {
    private final TalUltimatePlugin plugin;
    private BukkitTask task;

    public HudManager(TalUltimatePlugin plugin){ this.plugin=plugin; }

    public void start(){
        stop();
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()){
                update(p);
            }
        }, 20L, 40L); // 2초마다 업데이트
    }

    public void stop(){
        if (task != null) task.cancel();
        task = null;
    }

    private void update(Player p){
        Scoreboard sb = p.getScoreboard();
        if (sb == null || sb == Bukkit.getScoreboardManager().getMainScoreboard()){
            sb = Bukkit.getScoreboardManager().getNewScoreboard();
            p.setScoreboard(sb);
        }

        Objective obj = sb.getObjective("talhud");
        if (obj == null){
            obj = sb.registerNewObjective("talhud", "dummy", "§a§lTAL §7Status", RenderType.INTEGER);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        sb.getEntries().forEach(sb::resetScores);

        String title = TalUltimatePlugin.TITLES.getMeta(TalUltimatePlugin.TITLES.getCurrent(p)) != null
                ? TalUltimatePlugin.TITLES.getMeta(TalUltimatePlugin.TITLES.getCurrent(p)).display
                : TalUltimatePlugin.TITLES.getCurrent(p);

        addLine(obj, "§f이름: §a" + p.getName(), 5);
        addLine(obj, "§f레벨: §a" + p.getLevel(), 4);
        addLine(obj, "§f잔액: §a" + TalUltimatePlugin.ECON.get(p) + "원", 3);
        addLine(obj, "§f칭호: §a" + title, 2);
        addLine(obj, "§7", 1);
    }

    private void addLine(Objective obj, String text, int score){
        obj.getScore(text).setScore(score);
    }
}
