package com.talultimate.features;

import com.talultimate.TalUltimatePlugin;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    private final TalUltimatePlugin plugin;
    private final ConcurrentHashMap<UUID, Long> balances = new ConcurrentHashMap<>();

    public EconomyManager(TalUltimatePlugin plugin){ this.plugin = plugin; }

    public long get(Player p){ return balances.getOrDefault(p.getUniqueId(), 0L); }
    public void set(Player p, long value){ if (value<0) value=0; balances.put(p.getUniqueId(), value); }

    public void give(Player p, long baseAmount){
        long adj = Math.round(baseAmount * getMoneyMultiplier(p));
        set(p, get(p) + adj);
    }

    public boolean take(Player p, long amount){
        if (amount<=0) return true;
        long cur = get(p);
        if (cur < amount) return false;
        set(p, cur - amount); return true;
    }

    /** 현재 칭호의 money-multiplier 반환 (기본 1.00) */
    public double getMoneyMultiplier(Player p){
        var tm = TalUltimatePlugin.TITLES;
        var meta = tm.getMeta(tm.getCurrent(p));
        return meta != null ? meta.moneyMultiplier : 1.0;
    }

    /** 판매 금액 산정 시 보너스(+%) 적용 */
    public long applySellBonus(Player p, long base){
        var tm = TalUltimatePlugin.TITLES;
        var meta = tm.getMeta(tm.getCurrent(p));
        double bonus = (meta != null ? meta.sellBonus : 0.0);
        return Math.round(base * (1.0 + bonus));
    }
}
