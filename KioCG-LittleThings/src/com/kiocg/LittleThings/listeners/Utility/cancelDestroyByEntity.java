package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class cancelDestroyByEntity implements Listener {
    // 弹射物不伤害非生物实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelProjectileHit(final @NotNull ProjectileHitEvent e) {
        final Entity hitEntity = e.getHitEntity();

        if (hitEntity == null) {
            return;
        }

        //TODO 大版本更新时的世界名修改
        if (!"KioCG_17world".equals(hitEntity.getWorld().getName())) {
            return;
        }

        if (e.getEntity().getShooter() instanceof Player) {
            return;
        }

        if (!(hitEntity instanceof Mob)) {
            e.setCancelled(true);
        }
    }

    // 实体和爆炸不破坏悬挂实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelHangingBreakByEntity(final @NotNull HangingBreakByEntityEvent e) {
        //TODO 大版本更新时的世界名修改
        if (!"KioCG_17world".equals(e.getEntity().getWorld().getName())) {
            return;
        }

        if (!(e.getRemover() instanceof Player)) {
            e.setCancelled(true);
        }
    }
}
