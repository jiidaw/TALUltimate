package com.talultimate.features;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class LandManager {
    private final List<Claim> claims = new ArrayList<>();
    public LandManager(){ }
    public static record Claim(UUID owner, String world, int x1,int z1,int x2,int z2){}
    public boolean isInside(String world, int x, int z, UUID player){
        for (Claim c : claims){
            if (!c.world().equals(world)) continue;
            if (x>=Math.min(c.x1(),c.x2()) && x<=Math.max(c.x1(),c.x2())
             && z>=Math.min(c.z1(),c.z2()) && z<=Math.max(c.z1(),c.z2())) {
                return c.owner().equals(player);
            }
        }
        return true;
    }
    public void buy(Player p, int size, long price){
        Location l = p.getLocation();
        int half = size/2;
        Claim c = new Claim(p.getUniqueId(), l.getWorld().getName(), l.getBlockX()-half, l.getBlockZ()-half, l.getBlockX()+half, l.getBlockZ()+half);
        claims.add(c);
        p.sendMessage("§a땅 구매 완료: "+size+"x"+size);
    }
}
