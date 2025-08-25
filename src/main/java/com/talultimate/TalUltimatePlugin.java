package com.talultimate;

import com.talultimate.features.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TalUltimatePlugin extends JavaPlugin {

    // 전역 매니저/싱글턴 (필요한 것만)
    public static WarpManager WARPS;
    public static GachaManager GACHA;
    public static TitleManager TITLES; // 이미 있다면 기존 구현 사용

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 매니저 생성
        WARPS = new WarpManager(this);
        GACHA = new GachaManager(this);
        if (TITLES == null) TITLES = new TitleManager(this); // 이미 있으면 주석처리

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new AdvancementListener(this), this);
        getServer().getPluginManager().registerEvents(new LevelRewardListener(), this); // 무인자 생성자 기준

        // 커맨드 등록
        getCommand("menu").setExecutor(new UiMenuCommand(this));
        getCommand("gachaitem").setExecutor(new GachaCommand(this, false));
        getCommand("gachatitle").setExecutor(new GachaCommand(this, true));
        getCommand("settitle").setExecutor(new SetTitleCommand());           // 무인자
        getCommand("shopadmin").setExecutor(new ShopAdminCommand(this));     // plugin 필요
        getCommand("광장").setExecutor(new WarpCommand(this, "plaza", "tal.warp.plaza"));
        getCommand("결투장").setExecutor(new WarpCommand(this, "arena", "tal.warp.arena"));

        getLogger().info("TALUltimate loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TALUltimate disabled.");
    }
}
// (기존)
public final class TalUltimatePlugin extends JavaPlugin {

    public static WarpManager WARPS;
    public static GachaManager GACHA;
    public static TitleManager TITLES;

// ⬇⬇⬇ 여기 추가
    public static EconomyManager ECON;
// ⬆⬆⬆

// ...
    @Override
    public void onEnable() {
        saveDefaultConfig();

        WARPS = new WarpManager(this);
        GACHA = new GachaManager(this);
        if (TITLES == null) TITLES = new TitleManager(this);

// ⬇⬇⬇ 여기 추가
        ECON = new EconomyManager(this);
// ⬆⬆⬆

