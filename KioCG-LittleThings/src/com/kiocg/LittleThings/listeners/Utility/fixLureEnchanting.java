package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class fixLureEnchanting implements Listener {
    // 修复饵钓附魔
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fixVanishingCurse(final @NotNull PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.FISHING) {
            return;
        }

        final Player player = e.getPlayer();

        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getEnchantmentLevel(Enchantment.LURE) > 5) {
            itemStack.removeEnchantment(Enchantment.LURE);
            itemStack.addUnsafeEnchantment(Enchantment.LURE, 5);

            player.sendMessage("§a[§b豆渣子§a] §6已修复你手中钓竿的饵钓附魔属性.");
            e.setCancelled(true);
        }
    }
}
