package com.kiocg.LittleThings.listeners;

import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class Fix implements @NotNull Listener {
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

        if (!entity.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }

        if (entity instanceof ArmorStand && !(e.getDamager() instanceof Player)) {
            e.setCancelled(true);
        }
    }

    // 实体和爆炸不破坏悬挂实体
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelHangingBreakByEntity(final @NotNull HangingBreakByEntityEvent e) {
        if (!e.getEntity().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }

        final Entity remover = e.getRemover();
        if (!(remover instanceof Player)) {
            e.setCancelled(true);
        }
    }

    // 保护内部保留的物品名前缀
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            return;
        }

        final String renameText = e.getInventory().getRenameText();

        if (Pattern.matches("^(&[0-9a-zA-Z]){3}.*$", renameText) || Pattern.matches("^(&#[0-9a-zA-Z]{6}){3}.*$", renameText)) {
            e.setResult(null);
        }
    }
}
