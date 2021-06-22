package com.kiocg.LittleThings.listeners.Misc;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class copySign implements Listener {
    // 拷贝告示牌
    @EventHandler
    public void copySign(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || Objects.requireNonNull(e.getHand()) != EquipmentSlot.HAND) {
            return;
        }

        final Block block = e.getClickedBlock();

        if (!Objects.requireNonNull(block).getType().toString().endsWith("_SIGN")) {
            return;
        }

        final ItemStack itemStack = e.getItem();

        try {
            if (!Objects.requireNonNull(itemStack).getType().toString().endsWith("_SIGN")) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final Inventory inventory = e.getPlayer().getInventory();
        if (inventory.firstEmpty() != -1) {
            final ItemStack signStack = itemStack.clone();
            signStack.setAmount(1);

            final BlockStateMeta blockStateMeta = (BlockStateMeta) signStack.getItemMeta();

            if (blockStateMeta == null) {
                return;
            }

            Objects.requireNonNull(blockStateMeta).setBlockState(block.getState());
            signStack.setItemMeta(blockStateMeta);

            itemStack.setAmount(itemStack.getAmount() - 1);

            inventory.addItem(signStack);
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();

        if (!Objects.requireNonNull(itemStack).getType().toString().endsWith("_SIGN")) {
            return;
        }

        final String[] lines = ((Sign) ((BlockStateMeta) Objects.requireNonNull(itemStack.getItemMeta())).getBlockState()).getLines();

        if (lines[0].isEmpty() && lines[1].isEmpty() && lines[2].isEmpty() && lines[3].isEmpty()) {
            return;
        }

        final Sign signBlock = (Sign) e.getBlockPlaced().getState();
        signBlock.setLine(0, lines[0]);
        signBlock.setLine(1, lines[1]);
        signBlock.setLine(2, lines[2]);
        signBlock.setLine(3, lines[3]);
        signBlock.update();

        Bukkit.getScheduler().runTask(LittleThings.instance, () -> e.getPlayer().closeInventory());
    }
}
