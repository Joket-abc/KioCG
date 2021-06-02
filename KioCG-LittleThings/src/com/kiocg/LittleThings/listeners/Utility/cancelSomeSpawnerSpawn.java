package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class cancelSomeSpawnerSpawn implements Listener {
    // 动物或凋零骷髅刷怪笼禁止使用
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelSomeSpawnerSpawn(final @NotNull SpawnerSpawnEvent e) {
        final Entity entity = e.getEntity();

        if ("KioCG_OhTheDungeon".equals(entity.getWorld().getName())) {
            return;
        }

        if (!(entity instanceof Monster) || entity instanceof WitherSkeleton) {
            e.setCancelled(true);
        }
    }
}
