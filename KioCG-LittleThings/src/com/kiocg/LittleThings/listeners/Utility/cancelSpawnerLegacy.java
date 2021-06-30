package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class cancelSpawnerLegacy implements Listener {
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

    // 禁止使用生成蛋替换刷怪笼生物
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelSpawnerReplace(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || Objects.requireNonNull(e.getClickedBlock()).getType() != Material.SPAWNER) {
            return;
        }

        try {
            if (!Objects.requireNonNull(e.getItem()).getType().toString().endsWith("_SPAWN_EGG")) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (e.getPlayer().isOp()) {
            return;
        }

        e.setCancelled(true);
    }
}
