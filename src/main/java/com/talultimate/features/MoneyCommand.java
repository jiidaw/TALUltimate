package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.Util;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class MoneyCommand implements CommandExecutor {
    private final TalUltimatePlugin plugin;
    public MoneyCommand(TalUltimatePlugin plugin){ this.plugin = plugin; }

    @Override public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        switch (cmd.getName().toLowerCase()){
            case "money" -> p.sendMessage("§b잔액: §f" + Util.money(TalUltimatePlugin.ECON.get(p)));
            case "deposit" -> {
                if (args.length==0 || args[0].equalsIgnoreCase("all")) {
                    long sum = 0;
                    for (Map.Entry<Integer,String> e : TalUltimatePlugin.ECON.getNoteNames().entrySet()){
                        for (ItemStack is : p.getInventory().all(Material.PAPER).values()){
                            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()
                                    && e.getValue().equals(is.getItemMeta().getDisplayName())) {
                                sum += (long) e.getKey() * is.getAmount();
                                p.getInventory().removeItem(is);
                            }
                        }
                    }
                    for (Map.Entry<Material,Integer> e : TalUltimatePlugin.ECON.getSellPrice().entrySet()){
                        for (ItemStack is : p.getInventory().all(e.getKey()).values()){
                            sum += (long) e.getValue() * is.getAmount();
                            p.getInventory().removeItem(is);
                        }
                    }
                    TalUltimatePlugin.ECON.add(p, sum);
                    p.sendMessage("§a입금: +" + Util.money(sum));
                } else if (args[0].equalsIgnoreCase("note")){
                    long sum = 0;
                    for (Map.Entry<Integer,String> e : TalUltimatePlugin.ECON.getNoteNames().entrySet()){
                        for (ItemStack is : p.getInventory().all(Material.PAPER).values()){
                            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()
                                    && e.getValue().equals(is.getItemMeta().getDisplayName())) {
                                sum += (long) e.getKey() * is.getAmount();
                                p.getInventory().removeItem(is);
                            }
                        }
                    }
                    TalUltimatePlugin.ECON.add(p, sum);
                    p.sendMessage("§a지폐/동전 입금: +" + Util.money(sum));
                } else {
                    try{
                        Material m = Material.valueOf(args[0].toUpperCase());
                        int amount = args.length>1? Integer.parseInt(args[1]) : p.getInventory().all(m).values().stream().mapToInt(ItemStack::getAmount).sum();
                        int price = TalUltimatePlugin.ECON.getSellPrice().getOrDefault(m,0);
                        if (price<=0){ p.sendMessage("§e입금 대상이 아닙니다."); return true; }
                        int removed = 0;
                        for (ItemStack is : p.getInventory().all(m).values()){
                            int take = Math.min(is.getAmount(), amount-removed);
                            is.setAmount(is.getAmount()-take);
                            removed += take;
                            if (removed>=amount) break;
                        }
                        long sum = (long) price * removed;
                        TalUltimatePlugin.ECON.add(p, sum);
                        p.sendMessage("§a입금: " + m + " x"+removed+" → +" + Util.money(sum));
                    }catch (Exception ex){ p.sendMessage("§c사용법: /deposit [all|note|<material> <amount>]"); }
                }
            }
            case "withdraw" -> {
                if (args.length<1){ p.sendMessage("§c/withdraw <amount>"); return true; }
                long amt;
                try{ amt = Long.parseLong(args[0]); }catch(Exception e){ p.sendMessage("§c숫자를 입력하세요"); return true; }
                if (!TalUltimatePlugin.ECON.take(p, amt)){ p.sendMessage("§e잔액 부족"); return true; }
                int remain = (int) amt;
                int[] units = new int[]{50000,10000,5000,1000,500,100,10};
                for (int u: units){
                    int cnt = remain / u;
                    if (cnt<=0) continue;
                    String name = TalUltimatePlugin.ECON.getNoteNames().get(u);
                    if (name == null) continue;
                    while (cnt>0){
                        int give = Math.min(64, cnt);
                        ItemStack is = new ItemStack(Material.PAPER, give);
                        is.editMeta(meta -> meta.setDisplayName(name));
                        p.getInventory().addItem(is);
                        cnt -= give;
                    }
                    remain %= u;
                }
                p.sendMessage("§b출금: " + Util.money(amt) + " → 인벤토리에 노트/동전 지급");
            }
        }
        return true;
    }
}
