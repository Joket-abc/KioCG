package com.kiocg.InfiniteEnchant;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    static final Map<Player, BossBar> bossBarMap = new HashMap<>();

    public static void showBossBar(final @NotNull Player player, final int repairCost, final boolean canRepair) {
        final BossBar bossBar = bossBarMap.get(player);

        if (bossBar == null) {
            final BossBar bar = Bukkit.createBossBar((canRepair ? "§a" : "§c") + "附魔花费:" + repairCost, BarColor.RED, BarStyle.SOLID);
            bossBarMap.put(player, bar);
            bar.addPlayer(player);
        } else {
            bossBar.setTitle((canRepair ? "§a" : "§c") + "附魔花费:" + repairCost);
        }
    }

    public static void removeBossBar(final Player player) {
        try {
            bossBarMap.get(player).removeAll();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        bossBarMap.remove(player);
    }
}
