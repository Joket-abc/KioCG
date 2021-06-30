package com.kiocg.LittleThings.listeners.Fun;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class deathPenalty implements Listener {
    // 死亡惩罚
    @EventHandler
    public void creeperFirework(final @NotNull PlayerRespawnEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.deathpenalty.bypass")) {
            return;
        }

        player.setHealth(10.0);
        player.setFoodLevel(10);
    }
}
