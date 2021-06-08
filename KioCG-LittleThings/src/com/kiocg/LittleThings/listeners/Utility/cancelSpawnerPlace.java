package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class cancelSpawnerPlace implements Listener {
    // 禁止放置刷怪笼
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelSpawnerPlace(final @NotNull BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() != Material.SPAWNER || e.getPlayer().hasPermission("kiocg.cancelspawnerplace.bypass")) {
            return;
        }

        e.setCancelled(true);
    }
}
