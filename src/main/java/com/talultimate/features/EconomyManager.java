package com.talultimate.features;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    private final JavaPlugin plugin;
    private final ConcurrentHashMap<UUID, Long> balances = new ConcurrentHashMap<>();

    // 지폐/동전 이름표 (MoneyCommand에서 getNoteNames().get(key)로 씀)
    private final Map<String, String> noteNames = Map.of(
            "10", "10원", "100", "100원", "500", "500원",
            "1000", "1000원", "5000", "5000원",
            "10000", "10000원", "50000", "50000원"
    );

    public EconomyManager(JavaPlugin plugin) { this.plugin = plugin; }

    public long get(Player p) {
        return balances.getOrDefault(p.getUniqueId(), 0L);
    }

    public void set(Player p, long value) {
        if (value < 0) value = 0;
        balances.put(p.getUniqueId(), value);
    }

    public void give(Player p, long amount) {
        if (amount <= 0) return;
        set(p, get(p) + amount);
    }

    /** 금액 차감: 성공 시 true, 잔액 부족 시 false */
    public boolean take(Player p, long amount) {
        if (amount <= 0) return true;
        long cur = get(p);
        if (cur < amount) return false;
        set(p, cur - amount);
        return true;
    }

    /** MoneyCommand에서 사용하는 표기 맵 */
    public Map<String, String> getNoteNames() {
        return noteNames;
    }
}
