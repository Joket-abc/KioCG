package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class fixVanishingCurse implements Listener {
    // 死亡移除消失诅咒的物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fixVanishingCurse(final @NotNull PlayerDeathEvent e) {
        final Player player = e.getEntity();

        if (player.hasPermission("kiocg.vanishingcurse.keep")) {
            return;
        }

        for (final ItemStack itemStack : player.getInventory().getContents()) {
            try {
                if (Objects.requireNonNull(itemStack).containsEnchantment(Enchantment.VANISHING_CURSE)) {
                    itemStack.setAmount(0);
                }
            } catch (final @NotNull NullPointerException ignore) {
            }
        }
    }
}
