package com.talultimate;

import com.talultimate.features.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TalUltimatePlugin extends JavaPlugin {

    public static WarpManager WARPS;
    public static GachaManager GACHA;
    public static TitleManager TITLES;
    public static EconomyManager ECON;   // ★ 추가된 필드

    @Override
    public void onEnable() {
        // 기본 설정 파일 저장
        saveDefaultConfig();

        // 매니저 생성
        WARPS = new WarpManager(this);
        GACHA = new GachaManager(this);
        if (TITLES == null) TITLES = new TitleManager(this);
        ECON = new EconomyManager(this); // ★ 경제 매니저 초기화

        // 리스너 등록
        getServer().getPluginManager().registerEvents(new AdvancementListener(this), this);
        getServer().getPluginManager().registerEvents(new LevelRewardListener(), this);

        // 커맨드 등록
        getCommand("menu").setExecutor(new UiMenuCommand(this));
        getCommand("gachaitem").setExecutor(new GachaCommand(this, false));
        getCommand("gachatitle").setExecutor(new GachaCommand(this, true));
        getCommand("settitle").setExecutor(new SetTitleCommand());
        getCommand("shopadmin").setExecutor(new ShopAdminCommand(this));
        getCommand("광장").setExecutor(new WarpCommand(this, "plaza", "tal.warp.plaza"));
        getCommand("결투장").setExecutor(new WarpCommand(this, "arena", "tal.warp.arena"));

        getLogger().info("TALUltimate loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TALUltimate disabled.");
    }
}

