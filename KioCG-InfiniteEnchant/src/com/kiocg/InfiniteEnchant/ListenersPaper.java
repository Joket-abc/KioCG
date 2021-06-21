package com.kiocg.InfiniteEnchant;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ListenersPaper implements Listener {
    @EventHandler
    public void onPrepareGrindstone(final @NotNull PrepareResultEvent e) {
        if (e.getInventory().getType() != InventoryType.GRINDSTONE) {
            return;
        }

        try {
            if (Objects.requireNonNull(e.getResult()).getType() == Material.BOOK) {
                e.setResult(new ItemStack(Material.ENCHANTED_BOOK));
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }
}
