package com.talultimate;

import com.talultimate.features.*;        // TitleManager, EconomyManager 등
import com.talultimate.features.HudManager; // ← 명시해도 됨
import org.bukkit.plugin.java.JavaPlugin;

public final class TalUltimatePlugin extends JavaPlugin {

    public static TitleManager TITLES;
    public static EconomyManager ECON;

    private HudManager hud;                // ← 필드 추가

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 매니저/리스너 등록들…
        TITLES = new TitleManager(this);
        ECON   = new EconomyManager(this);
        getServer().getPluginManager().registerEvents(new AdvancementListener(this), this);
        getServer().getPluginManager().registerEvents(new LevelRewardListener(), this);

        // ★ HUD 시작은 '메서드 안'에서!
        hud = new HudManager(this);
        hud.start();

        getLogger().info("TALUltimate loaded!");
    }

    @Override
    public void onDisable() {
        // HUD 정리
        if (hud != null) hud.stop();
        getLogger().info("TALUltimate disabled.");
    }
}
