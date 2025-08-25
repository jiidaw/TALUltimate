package com.talultimate;

import com.talultimate.features.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TalUltimatePlugin extends JavaPlugin {

    public static EconomyManager ECON;
    public static TitleManager TITLES;
    public static HudManager HUD;
    public static WarpManager WARPS;
    public static LandManager LAND;
    public static TpaHomeManager TPAHOME;
    public static GachaManager GACHA;
    public static ShopManager SHOP;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ECON = new EconomyManager(this);
        TITLES = new TitleManager(this);
        HUD = new HudManager(this);
        WARPS = new WarpManager(this);
        LAND = new LandManager(this);
        TPAHOME = new TpaHomeManager();
        GACHA = new GachaManager(this);
        SHOP = new ShopManager(this);

        register(new JoinQuitListener(this));
        register(new AdvancementListener(this));
        register(new LevelRewardListener(this));
        register(new LandProtectListener());

        getCommand("menu").setExecutor(new UiMenuCommand(this));
        getCommand("money").setExecutor(new MoneyCommand(this));
        getCommand("deposit").setExecutor(new MoneyCommand(this));
        getCommand("withdraw").setExecutor(new MoneyCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("광장").setExecutor(new WarpCommand(this, "plaza", "tal.warp.plaza"));
        getCommand("결투장").setExecutor(new WarpCommand(this, "arena", "tal.warp.arena"));
        getCommand("tpa").setExecutor(TPAHOME);
        getCommand("tpaccept").setExecutor(TPAHOME);
        getCommand("tpdeny").setExecutor(TPAHOME);
        getCommand("sethome").setExecutor(TPAHOME);
        getCommand("home").setExecutor(TPAHOME);
        getCommand("delhome").setExecutor(TPAHOME);
        getCommand("addtitle").setExecutor(new TitleAdminCommand(this, true));
        getCommand("removetitle").setExecutor(new TitleAdminCommand(this, false));
        getCommand("settitle").setExecutor(new SetTitleCommand(this));
        getCommand("shopadmin").setExecutor(new ShopAdminCommand(this));
        getCommand("gachatitle").setExecutor(new GachaCommand(this, true));
        getCommand("gachaitem").setExecutor(new GachaCommand(this, false));

        HUD.start();
        getLogger().info("TALUltimate loaded (full pack).");
    }

    @Override
    public void onDisable() {
        if (HUD != null) HUD.stop();
        getLogger().info("TALUltimate disabled.");
    }

    private void register(Listener l) { Bukkit.getPluginManager().registerEvents(l, this); }
}
// ... 기존 초기화/등록 아래
new HudManager(this).start();
