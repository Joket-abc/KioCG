package com.kiocg.LittleThings.listeners.Misc;

import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.meta.ItemMeta;
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

        if (!MaterialTags.SIGNS.isTagged(Objects.requireNonNull(block))) {
            return;
        }

        final ItemStack itemStack = e.getItem();

        try {
            if (!MaterialTags.SIGNS.isTagged(Objects.requireNonNull(itemStack))) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final Inventory inventory = e.getPlayer().getInventory();
        if (inventory.firstEmpty() != -1) {
            itemStack.setAmount(itemStack.getAmount() - 1);

            final ItemStack signStack = itemStack.clone().asOne();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(Objects.requireNonNull(itemStack.getI18NDisplayName())).append(Component.text("(拷贝)")));

            final BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
            Objects.requireNonNull(blockStateMeta).setBlockState(block.getState());
            signStack.setItemMeta(blockStateMeta);

            inventory.addItem(signStack);
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();

        if (!MaterialTags.SIGNS.isTagged(Objects.requireNonNull(itemStack)) && !itemStack.displayName().toString().endsWith("(拷贝)")) {
            return;
        }

        final Sign signBlock = (Sign) e.getBlockPlaced().getState();
        final Sign signItem = (Sign) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState();

        signBlock.line(1, signItem.line(1));
        signBlock.line(2, signItem.line(2));
        signBlock.line(3, signItem.line(3));
        signBlock.line(4, signItem.line(4));
    }
}
