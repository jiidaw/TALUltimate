package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.WeightedPicker;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GachaManager {
    private final TalUltimatePlugin plugin;
    public GachaManager(TalUltimatePlugin plugin){ this.plugin = plugin; }

    public void gachaItem(Player p, boolean premium){
        int price = plugin.getConfig().getInt("gacha.item."+(premium?"premium-price":"normal-price"), 1000);
        if (!TalUltimatePlugin.ECON.take(p, price)){ p.sendMessage("§e잔액 부족"); return; }
        WeightedPicker<R> picker = new WeightedPicker<>();
        for (var m : plugin.getConfig().getMapList("gacha.item.pool")){
            String type = (String)m.get("type");
            int weight = ((Number)m.get("weight")).intValue();
            R r = new R(type,(String)m.getOrDefault("material",""),((Number)m.getOrDefault("amount",1)).intValue());
            picker.add(r, weight);
        }
        R pick = picker.pick();
        if (pick==null){ p.sendMessage("§e당첨 없음"); return; }
        switch (pick.type){
            case "LOSE" -> p.sendMessage("§7꽝…");
            case "ORE","SEED" -> {
                try{
                    Material mat = Material.valueOf(pick.material);
                    p.getInventory().addItem(new ItemStack(mat, pick.amount));
                    p.sendMessage("§a당첨: "+mat+" x"+pick.amount);
                }catch(Exception ex){ p.sendMessage("§e보상 오류"); }
            }
            case "VOUCHER_FORCE_TP" -> p.sendMessage("§d강제 TP권 x1 (임시)");
            case "VOUCHER_OP_SUMMON" -> p.sendMessage("§c운영자 소환권 x1 (임시)");
        }
    }
    record R(String type, String material, int amount){}
    public void gachaTitle(Player p){
        int price = plugin.getConfig().getInt("gacha.title.price", 5000);
        if (!TalUltimatePlugin.ECON.take(p, price)){ p.sendMessage("§e잔액 부족"); return; }
        var weights = plugin.getConfig().getConfigurationSection("gacha.title.weights");
        if (weights==null){ p.sendMessage("§e가중치 미설정"); return; }
        WeightedPicker<String> picker = new WeightedPicker<>();
        for (String g : weights.getKeys(false)) picker.add(g, weights.getInt(g));
        String grade = picker.pick();
        String chosenId = null;
        var list = plugin.getConfig().getConfigurationSection("titles.list");
        for (String id : list.getKeys(false)){
            var s = list.getConfigurationSection(id);
            if (!grade.equalsIgnoreCase(s.getString("grade"))) continue;
            if (!"GACHA".equalsIgnoreCase(s.getConfigurationSection("obtained-by").getString("type"))) continue;
            chosenId = id; break;
        }
        if (chosenId==null){ p.sendMessage("§e해당 등급 칭호가 설정되어 있지 않습니다."); return; }
        TalUltimatePlugin.TITLES.grant(p, chosenId, true);
    }
}
class GachaCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin; private final boolean title;
    public GachaCommand(TalUltimatePlugin plugin, boolean title){ this.plugin=plugin; this.title=title; }
    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a){
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        if (title) TalUltimatePlugin.GACHA.gachaTitle(p);
        else TalUltimatePlugin.GACHA.gachaItem(p, a.length>0 && a[0].equalsIgnoreCase("premium"));
        return true;
    }
}
