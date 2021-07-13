package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class cancelSpawnerPlace implements Listener {
    /**
     * 此类保险起见勿删!
     */

    // 禁止放置刷怪笼
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelSpawnerPlace(final @NotNull BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() != Material.SPAWNER || e.getPlayer().isOp()) {
            return;
        }

        e.setCancelled(true);
    }
}
