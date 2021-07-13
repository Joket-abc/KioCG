package com.kiocg.LittleThings.listeners.Misc;

import com.destroystokyo.paper.MaterialTags;
import com.kiocg.LittleThings.LittleThings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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

import java.util.List;
import java.util.Objects;

public class copySign implements Listener {
    // 拷贝告示牌
    @EventHandler
    public void copySign(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) {
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

        final Player player = e.getPlayer();
        if (player.isSneaking()) {
            return;
        }

        final BlockState blockState = block.getState();
        for (final Component component : ((Sign) blockState).lines()) {
            if (component.toString().contains("[")) {
                player.sendMessage("§a[§b豆渣子§a] §6告示牌包含特殊符号, 无法复制.");
                e.setCancelled(true);
                return;
            }
        }

        final Inventory inventory = player.getInventory();
        if (inventory.firstEmpty() != -1) {
            final ItemStack signStack = itemStack.clone().asOne();

            final BlockStateMeta blockStateMeta = (BlockStateMeta) signStack.getItemMeta();

            if (blockStateMeta == null) {
                return;
            }

            blockStateMeta.setBlockState(blockState);
            signStack.setItemMeta(blockStateMeta);

            itemStack.setAmount(itemStack.getAmount() - 1);

            inventory.addItem(signStack);
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();

        if (!MaterialTags.SIGNS.isTagged(itemStack)) {
            return;
        }

        final List<Component> lines = ((Sign) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState()).lines();

        if (lines.get(0).toString().isEmpty() && lines.get(1).toString().isEmpty() && lines.get(2).toString().isEmpty() && lines.get(3).toString().isEmpty()) {
            return;
        }

        final Sign signBlock = (Sign) e.getBlockPlaced().getState();
        signBlock.line(0, lines.get(0));
        signBlock.line(1, lines.get(0));
        signBlock.line(2, lines.get(0));
        signBlock.line(3, lines.get(0));
        signBlock.update();

        Bukkit.getScheduler().runTask(LittleThings.instance, () -> e.getPlayer().closeInventory());
    }
}
