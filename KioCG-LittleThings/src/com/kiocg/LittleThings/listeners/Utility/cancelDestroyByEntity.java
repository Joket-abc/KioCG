package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class cancelDestroyByEntity implements Listener {
    // 实体不破坏盔甲架
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelArmorStandDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();

        if (!"KioCG_world".equals(entity.getWorld().getName())) {
            return;
        }

        if (entity instanceof ArmorStand && !(e.getDamager() instanceof Player)) {
            e.setCancelled(true);
        }
    }

    // 实体和爆炸不破坏悬挂实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelHangingBreakByEntity(final @NotNull HangingBreakByEntityEvent e) {
        if (!"KioCG_world".equals(e.getEntity().getWorld().getName())) {
            return;
        }

        if (!(e.getRemover() instanceof Player)) {
            e.setCancelled(true);
        }
    }
}
