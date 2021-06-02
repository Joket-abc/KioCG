package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class randomPlace implements Listener {
    // 随机放置方块
    @EventHandler(ignoreCancelled = true)
    public void randomPlace(final @NotNull BlockPlaceEvent e) {
        final PlayerInventory playerInventory = e.getPlayer().getInventory();

        if (playerInventory.getItemInOffHand().getType() != Material.STICK) {
            return;
        }

        final List<Integer> slots = new ArrayList<>();
        for (int i = 0; i <= 8; ++i) {
            final ItemStack itemStack = playerInventory.getItem(i);
            if (itemStack != null && itemStack.getType().isBlock()) {
                slots.add(i);
            }
        }

        playerInventory.setHeldItemSlot(slots.get(new Random().nextInt(slots.size())));
    }
}
