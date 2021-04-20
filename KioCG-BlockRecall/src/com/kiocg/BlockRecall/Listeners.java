package com.kiocg.BlockRecall;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        Utils.lastBlockState.remove(player);
        Utils.lastBlockItemStack.remove(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final BlockState blockState = e.getBlockPlaced().getState();

        // 容器容易出事情
        if (blockState instanceof TileState) {
            return;
        }

        final ItemStack itemStackClone = e.getItemInHand().clone();

        // 防止放下的方块类型和手中物品类型不一致 (eg.打火石点火)
        if (!itemStackClone.getType().equals(blockState.getType())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Utils.lastBlockState.put(player, blockState);
        itemStackClone.setAmount(1);
        Utils.lastBlockItemStack.put(player, itemStackClone);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        final Player player = e.getPlayer();

        if (!Utils.lastBlockState.containsKey(player) || player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final Block block = e.getClickedBlock();
        final BlockState blockState = Objects.requireNonNull(block).getState();

        // 防止最后放置的方块一直被记录, 优化体验
        if (!Utils.lastBlockState.get(player).equals(blockState)) {
            Utils.lastBlockState.remove(player);
            Utils.lastBlockItemStack.remove(player);
            return;
        }

        // 台阶问题修复
        final BlockData blockData = blockState.getBlockData();
        if (blockData instanceof Slab && ((Slab) blockData).getType().equals(Slab.Type.DOUBLE)) {
            Utils.lastBlockState.remove(player);
            Utils.lastBlockItemStack.remove(player);
            return;
        }

        // 判断玩家能否破坏方块, 并且能让Coreprotect记录数据
        final BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);

        // 如果事件被取消则玩家没有权限撤回方块. 并且其他插件可能会修改方块, 需要再次判断
        if (event.isCancelled() || !Utils.lastBlockState.get(player).equals(blockState)) {
            Utils.lastBlockState.remove(player);
            Utils.lastBlockItemStack.remove(player);
            return;
        }

        final Location loc = block.getLocation();

        block.getWorld().dropItemNaturally(loc, Utils.lastBlockItemStack.get(player));
        block.setType(Material.AIR);

        player.playSound(loc.toCenterLocation(), block.getSoundGroup().getBreakSound(), 1.0F, 1.0F);
        e.setCancelled(true);

        Utils.lastBlockState.remove(player);
        Utils.lastBlockItemStack.remove(player);
    }
}
