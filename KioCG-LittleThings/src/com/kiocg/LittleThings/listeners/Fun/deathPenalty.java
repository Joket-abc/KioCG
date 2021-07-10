package com.kiocg.LittleThings.listeners.Fun;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class deathPenalty implements Listener {
    // 死亡惩罚
    @EventHandler
    public void deathPenalty(final @NotNull PlayerRespawnEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.deathpenalty.bypass")) {
            return;
        }

        Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
            player.setHealth(10.0);
            player.setFoodLevel(10);
        });
    }
}
