package com.talultimate.features;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class TpaHomeManager implements CommandExecutor {
    private final Map<UUID, UUID> tpaReq = new HashMap<>();
    private final Map<UUID, Location> home = new HashMap<>();

    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a){
        if (!(s instanceof Player p)){ s.sendMessage("player only"); return true; }
        switch (c.getName().toLowerCase()){
            case "tpa" -> {
                if (a.length<1){ p.sendMessage("§e/tpa <player>"); return true; }
                Player t = p.getServer().getPlayer(a[0]);
                if (t==null){ p.sendMessage("§c오프라인"); return true; }
                tpaReq.put(t.getUniqueId(), p.getUniqueId());
                t.sendMessage("§e"+p.getName()+" 님이 TP요청을 보냈습니다. /tpaccept 또는 /tpdeny");
                p.sendMessage("§a요청 전송");
            }
            case "tpaccept" -> {
                UUID from = tpaReq.remove(p.getUniqueId());
                if (from==null){ p.sendMessage("§e대기 중인 요청 없음"); return true; }
                Player f = p.getServer().getPlayer(from);
                if (f!=null) f.teleport(p.getLocation());
                p.sendMessage("§a수락 완료");
            }
            case "tpdeny" -> { tpaReq.remove(p.getUniqueId()); p.sendMessage("§7거절 완료"); }
            case "sethome" -> { home.put(p.getUniqueId(), p.getLocation()); p.sendMessage("§a홈 저장"); }
            case "home" -> {
                Location h = home.get(p.getUniqueId());
                if (h==null){ p.sendMessage("§e홈이 없습니다."); return true; }
                p.teleport(h); p.sendMessage("§a홈으로 이동");
            }
            case "delhome" -> { home.remove(p.getUniqueId()); p.sendMessage("§7홈 삭제"); }
        }
        return true;
    }
}
