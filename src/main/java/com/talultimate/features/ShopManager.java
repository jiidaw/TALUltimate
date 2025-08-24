package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

public class ShopManager {
    private final TalUltimatePlugin plugin;
    public ShopManager(TalUltimatePlugin plugin){ this.plugin = plugin; }
    public boolean setPrice(String mat, int price){
        try{
            Material m = Material.valueOf(mat.toUpperCase());
            TalUltimatePlugin.ECON.getSellPrice().put(m, price);
            plugin.getConfig().set("economy.ore-prices."+m.name(), price);
            plugin.saveConfig();
            return true;
        }catch(Exception e){ return false; }
    }
}
class ShopAdminCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin;
    public ShopAdminCommand(TalUltimatePlugin plugin){ this.plugin = plugin; }
    @Override public boolean onCommand(@NotNull CommandSender s, @NotNull Command c,@NotNull String l,@NotNull String[] a){
        if (!s.hasPermission("tal.admin")){ s.sendMessage("§c권한 없음"); return true; }
        if (a.length<2){ s.sendMessage("§e사용법: /shopadmin <MATERIAL> <price>"); return true; }
        boolean ok = TalUltimatePlugin.SHOP.setPrice(a[0], Integer.parseInt(a[1]));
        s.sendMessage(ok? "§a설정됨" : "§c재료명을 확인하세요");
        return true;
    }
}
