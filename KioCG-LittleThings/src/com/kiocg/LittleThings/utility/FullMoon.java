package com.kiocg.LittleThings.utility;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.world.MoonPhase;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FullMoon {
    private final @Nullable World world = Bukkit.getWorld("KioCG_world");
    private final BossBar bossBar = Bukkit.createBossBar("FullMoon", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);

    public FullMoon() {
        bossBar.setVisible(false);
        this.FullMoonTask();
    }

    // 满月特殊效果
    private void FullMoonTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LittleThings.instance, () -> {
            if (Objects.requireNonNull(world).getMoonPhase() == MoonPhase.NEW_MOON) {
                final List<Player> players = bossBar.getPlayers();

                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (!players.contains(player)) {
                        bossBar.addPlayer(player);
                    }
                }
                return;
            }

            bossBar.removeAll();
        }, 20L * 10L, 20L * 10L);
    }
}
