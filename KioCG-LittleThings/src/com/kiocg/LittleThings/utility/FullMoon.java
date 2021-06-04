package com.kiocg.LittleThings.utility;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.world.MoonPhase;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FullMoon {
    private final World world;

    private final @NotNull BossBar bossBar;

    public FullMoon() {
        //TODO 大版本更新时的世界名修改
        world = Objects.requireNonNull(Bukkit.getWorld("KioCG_world"));

        bossBar = Bukkit.createBossBar("FullMoon", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);
        bossBar.setVisible(false);

        this.FullMoonTask();
    }

    // 满月特殊效果
    private void FullMoonTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LittleThings.instance, () -> {
            if (world.getMoonPhase() == MoonPhase.NEW_MOON) {
                Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
                return;
            }

            bossBar.removeAll();
        }, 20L * 10L, 20L * 10L);
    }
}
