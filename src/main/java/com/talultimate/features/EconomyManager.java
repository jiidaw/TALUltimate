package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import com.talultimate.core.Util;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class EconomyManager {
    private final TalUltimatePlugin plugin;
    private final Map<UUID, Long> balance = new HashMap<>();
    private final Map<Material, Integer> sellPrice = new HashMap<>();
    private final Map<Integer, String> noteNames = new LinkedHashMap<>();
    public final int levelReward;

    public EconomyManager(TalUltimatePlugin plugin) {
        this.plugin = plugin;
        ConfigurationSection ore = plugin.getConfig().getConfigurationSection("economy.ore-prices");
        if (ore != null) for (String k : ore.getKeys(false)) sellPrice.put(Material.valueOf(k), ore.getInt(k));
        ConfigurationSection crop = plugin.getConfig().getConfigurationSection("economy.crop-prices");
        if (crop != null) for (String k : crop.getKeys(false)) sellPrice.put(Material.valueOf(k), crop.getInt(k));
        ConfigurationSection notes = plugin.getConfig().getConfigurationSection("notes");
        if (notes != null) for (String k : notes.getKeys(false)) noteNames.put(Integer.parseInt(k), notes.getString(k));
        levelReward = plugin.getConfig().getInt("economy.level-reward-per", 2000);
    }

    public long get(Player p){ return balance.getOrDefault(p.getUniqueId(), (long) plugin.getConfig().getInt("economy.start-money",0)); }
    public void set(Player p, long v){ balance.put(p.getUniqueId(), Math.max(0, v)); }
    public void add(Player p, long v){ set(p, get(p)+v); }
    public boolean take(Player p, long v){ if (get(p) < v) return false; add(p,-v); return true; }
    public Map<Material,Integer> getSellPrice(){ return sellPrice; }
    public Map<Integer,String> getNoteNames(){ return noteNames; }

    public long sell(Player p, boolean all){
        long earned = 0;
        if (all){
            for (ItemStack is : p.getInventory().getContents()){
                if (is==null) continue;
                Integer price = sellPrice.get(is.getType());
                if (price==null) continue;
                earned += (long) price * is.getAmount();
                is.setAmount(0);
            }
        }else{
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is!=null){
                Integer price = sellPrice.get(is.getType());
                if (price!=null){
                    earned += (long) price * is.getAmount();
                    p.getInventory().setItemInMainHand(null);
                }
            }
        }
        add(p, earned);
        if (earned>0) p.sendMessage("§a판매 완료! +" + Util.money(earned));
        else p.sendMessage("§e판매 가능한 아이템이 없습니다.");
        return earned;
    }
}
