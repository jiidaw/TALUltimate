package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;

public class UiMenuCommand implements CommandExecutor, Listener {
    public UiMenuCommand(TalUltimatePlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @Override public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        Inventory ui = Bukkit.createInventory(null, 27, "§0메뉴");
        ui.setItem(11, icon(Material.EMERALD, "§a상점(판매)", "클릭하여 판매(/sell)"));
        ui.setItem(13, icon(Material.PAPER, "§e칭호", "현재 칭호 변경(/settitle)"));
        ui.setItem(15, icon(Material.NETHER_STAR, "§d뽑기", "칭호/아이템 뽑기"));
        ui.setItem(22, icon(Material.GRASS_BLOCK, "§2땅 구매", "4x4, 8x8, 16x16"));
        p.openInventory(ui);
        p.playSound(p, Sound.UI_BUTTON_CLICK,1,1);
        return true;
    }
    private ItemStack icon(Material m,String name,String lore){
        ItemStack is = new ItemStack(m);
        is.editMeta(meta -> { meta.setDisplayName(name); meta.setLore(java.util.List.of("§7"+lore)); });
        return is;
    }
    @EventHandler public void onClick(InventoryClickEvent e){
        if (e.getView().getTitle().equals("§0메뉴")){
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player p)) return;
            if (e.getCurrentItem()==null) return;
            switch (e.getCurrentItem().getType()){
                case EMERALD -> p.performCommand("sell all");
                case PAPER -> p.sendMessage("§e/settitle <titleId> 로 칭호 변경");
                case NETHER_STAR -> p.performCommand("gachaitem");
                case GRASS_BLOCK -> p.sendMessage("§a땅 구매는 추후 GUI 확장 예정");
            }
            p.closeInventory();
        }
    }
}
