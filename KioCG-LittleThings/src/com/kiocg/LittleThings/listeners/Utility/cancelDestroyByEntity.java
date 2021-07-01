package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class cancelDestroyByEntity implements Listener {
    // 实体不破坏非生物实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();

        //TODO 大版本更新时的世界名修改
        if (!"KioCG_17world".equals(entity.getWorld().getName())) {
            return;
        }

        if (!(entity instanceof Player) && !(entity instanceof Mob) && !(e.getDamager() instanceof Player)) {
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
