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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        try {
            Utils.particleTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }

        Utils.particleTasks.remove(player);
    }

    // 破坏屏障
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Block block = e.getClickedBlock();

        if (Objects.requireNonNull(block).getType() != Material.BARRIER) {
            return;
        }

        final Player player = e.getPlayer();
        final ItemStack offItemStack = player.getInventory().getItemInOffHand();

        if (offItemStack.getType() != Material.BARRIER) {
            return;
        }

        final ItemMeta offItemMeta = offItemStack.getItemMeta();
        if (Objects.requireNonNull(offItemMeta).hasDisplayName() || offItemMeta.hasCustomModelData()) {
            return;
        }

        // 判断玩家能否破坏方块, 并且能让Coreprotect记录数据
        final BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);

        // 如果事件被取消则玩家没有权限破坏屏障
        if (event.isCancelled()) {
            return;
        }

        e.setCancelled(true);

        block.setType(Material.AIR);

        final Location location = block.getLocation();
        block.getWorld().dropItemNaturally(location, new ItemStack(Material.BARRIER));
        player.playSound(location.add(0.5, 0.5, 0.5), block.getBlockData().getSoundGroup().getBreakSound(), 1.0F, 1.0F);
    }

    //TODO 切换光源方块亮度等级
}
