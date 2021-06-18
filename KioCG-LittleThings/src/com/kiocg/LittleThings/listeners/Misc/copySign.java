package com.kiocg.LittleThings.listeners.Misc;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
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

        if (!MaterialTags.SIGNS.isTagged(Objects.requireNonNull(itemStack))) {
            return;
        }

        final Inventory inventory = e.getPlayer().getInventory();
        if (inventory.firstEmpty() != -1) {
            itemStack.setAmount(itemStack.getAmount() - 1);

            final ItemStack sign = itemStack.clone().asOne();
            final BlockDataMeta signBlockDataMeta = (BlockDataMeta) sign.getItemMeta();
            signBlockDataMeta.setBlockData(block.getBlockData());
            sign.setItemMeta(signBlockDataMeta);

            inventory.addItem(sign);
            e.setCancelled(true);
        }
    }
}
