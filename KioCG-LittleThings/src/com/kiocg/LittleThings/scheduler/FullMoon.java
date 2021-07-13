package com.kiocg.LittleThings.scheduler;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.world.MoonPhase;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FullMoon implements Listener {
    private final World world;

    private final @NotNull BossBar bossBar;

    public FullMoon() {
        //TODO 大版本更新时的世界名修改
        world = Objects.requireNonNull(Bukkit.getWorld("KioCG_17world"));

        bossBar = Bukkit.createBossBar("§c天干物燥 小心火烛", BarColor.PURPLE, BarStyle.SOLID, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);

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

    @EventHandler
    public void cancelEntityDamageByEntity(final @NotNull PlayerJoinEvent e) {
        if (world.getMoonPhase() == MoonPhase.NEW_MOON) {
            bossBar.addPlayer(e.getPlayer());
        }
    }
}
