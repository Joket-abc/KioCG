package com.kiocg.PantsCoins;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();

        if (itemStack.getType() == Material.BARRIER) {
            final ItemMeta itemMeta = itemStack.getItemMeta();

            // 自定义方块
            if (itemMeta.hasCustomModelData()) {
                final int customModelData = itemMeta.getCustomModelData();
                if (1001 <= customModelData && customModelData != 1054 && customModelData <= 1160) {
                    BlockManager.setCustomBlock(e.getBlockPlaced(), customModelData);
                    return;
                }
            }

            // 自定义物品
            if (itemMeta.hasDisplayName()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        if (!MaterialTags.MUSHROOM_BLOCKS.isTagged(block)) {
            return;
        }

        try {
            block.getWorld().dropItemNaturally(block.getLocation(), Objects.requireNonNull(BlockManager.getCustomBlockAsItemStack(block)));
        } catch (final @NotNull NullPointerException ignore) {
            // 是原版蘑菇方块
            return;
        }

        e.setDropItems(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        final ItemStack itemStack = e.getResult();
        if (itemStack != null && itemStack.getType() == Material.BARRIER) {
            e.setResult(null);
        }
    }
}
