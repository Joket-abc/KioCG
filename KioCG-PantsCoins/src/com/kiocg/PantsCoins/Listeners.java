package com.kiocg.PantsCoins;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Listeners implements @NotNull Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();
        if (itemStack.getType().equals(Material.BARRIER) && itemStack.getItemMeta().hasDisplayName()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        final ItemStack itemStack = e.getResult();
        if (itemStack != null && itemStack.getType().equals(Material.BARRIER)) {
            e.setResult(null);
        }
    }
}
