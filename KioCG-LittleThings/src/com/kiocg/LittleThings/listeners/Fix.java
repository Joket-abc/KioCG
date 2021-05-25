package com.kiocg.LittleThings.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Fix implements Listener {
    // 死亡移除消失诅咒的物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fixVanishingCurse(final @NotNull PlayerDeathEvent e) {
        for (final ItemStack itemStack : e.getEntity().getInventory().getContents()) {
            try {
                if (Objects.requireNonNull(itemStack).containsEnchantment(Enchantment.VANISHING_CURSE)) {
                    itemStack.setAmount(0);
                }
            } catch (final @NotNull NullPointerException ignore) {
            }
        }
    }

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

        final Entity remover = e.getRemover();
        if (!(remover instanceof Player)) {
            e.setCancelled(true);
        }
    }

    // 动物或凋零骷髅刷怪笼禁止使用
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelSpawnerSpawn(final @NotNull SpawnerSpawnEvent e) {
        if (!(e.getEntity() instanceof Monster) || e.getEntity() instanceof WitherSkeleton) {
            e.setCancelled(true);
        }
    }
}
