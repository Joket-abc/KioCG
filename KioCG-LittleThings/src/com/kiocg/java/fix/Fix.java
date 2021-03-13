package com.kiocg.java.fix;

import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Fix implements Listener {
    // 死亡移除消失诅咒的物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fixVanishingCurse(final PlayerDeathEvent e) {
        for (final ItemStack itemStack : e.getEntity().getInventory().getContents()) {
            // 存储了此物品栏所有物品的数组. 个别条目可能为null
            //noinspection ConstantConditions
            if (itemStack != null && itemStack.containsEnchantment(Enchantment.VANISHING_CURSE)) {
                itemStack.setAmount(0);
            }
        }
    }

    // 修复没有AI的生物
    @EventHandler(ignoreCancelled = true)
    public void fixAI(final PlayerInteractEntityEvent e) {
        final Entity entityClicked = e.getRightClicked();
        if (!(entityClicked instanceof Mob)) {
            return;
        }

        final Mob mob = (Mob) entityClicked;
        if (!mob.isCollidable()) {
            mob.setCollidable(true);
        }
        if (!mob.hasAI()) {
            mob.setAI(true);
        }
        if (!mob.isAware()) {
            mob.setAware(true);
        }
    }

    // 实体不破坏盔甲架
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelArmorStandDamageByEntity(final EntityDamageByEntityEvent e) {
        if (!e.getEntity().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }

        if (e.getEntityType().equals(EntityType.ARMOR_STAND) && !e.getDamager().getType().equals(EntityType.PLAYER)) {
            e.setCancelled(true);
        }
    }

    // 实体和爆炸不破坏悬挂实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelHangingBreakByEntity(final HangingBreakByEntityEvent e) {
        if (!e.getEntity().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }

        if (e.getRemover() == null || !e.getRemover().getType().equals(EntityType.PLAYER)) {
            e.setCancelled(true);
        }
    }
}
