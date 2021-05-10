package com.kiocg.BuildBarrier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (Utils.barrierPlayers.contains(player)) {
            Utils.barrierPlayers.remove(player);
            Utils.stopBarrier(player);
        }
    }

    // 破坏屏障
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        final Block block = e.getClickedBlock();

        if (!Objects.requireNonNull(block).getType().equals(Material.BARRIER)) {
            return;
        }

        final PlayerInventory playerInventory = e.getPlayer().getInventory();

        final ItemStack offItemStack = playerInventory.getItemInOffHand();
        if (!offItemStack.getType().equals(Material.BARRIER)) {
            return;
        }
        final ItemStack mainItemStack = playerInventory.getItemInMainHand();
        if (!mainItemStack.getType().equals(Material.BARRIER)) {
            return;
        }

        final ItemMeta offItemMeta = offItemStack.getItemMeta();
        if (offItemMeta.hasDisplayName() || offItemMeta.hasCustomModelData()) {
            return;
        }
        final ItemMeta mainItemMeta = mainItemStack.getItemMeta();
        if (mainItemMeta.hasDisplayName() || mainItemMeta.hasCustomModelData()) {
            return;
        }

        final Player player = e.getPlayer();

        // 判断玩家能否破坏方块, 并且能让Coreprotect记录数据
        final BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);

        // 如果事件被取消则玩家没有权限撤回方块
        if (event.isCancelled()) {
            return;
        }

        e.setCancelled(true);

        block.setType(Material.AIR);

        final Location location = block.getLocation();
        block.getWorld().dropItemNaturally(location, new ItemStack(Material.BARRIER));
        player.playSound(location, block.getSoundGroup().getBreakSound(), 1.0F, 1.0F);
    }
}
