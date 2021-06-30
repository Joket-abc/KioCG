package com.kiocg.BuildBarrier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        Utils.cancelParticleTask(player);
        Utils.particleTasks.remove(player);
    }

    @EventHandler
    public void onPlayerItemHeld(final @NotNull PlayerItemHeldEvent e) {
        final Player player = e.getPlayer();
        final Material material;
        try {
            material = Objects.requireNonNull(player.getInventory().getItem(e.getNewSlot())).getType();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (material == Material.BARRIER) {
            Utils.sandBarrierTask(player);
        } else if (material == Material.LIGHT) {
            Utils.sandBarrierTask(player);
        } else {
            Utils.cancelParticleTask(player);
        }
    }

    // 破坏屏障与末地传送门框架
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Block block = e.getClickedBlock();
        final Material material = Objects.requireNonNull(block).getType();
        final Player player;

        if (material == Material.BARRIER) {
            player = e.getPlayer();

            final ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType() != Material.BARRIER) {
                return;
            }

            final ItemMeta itemMeta = itemStack.getItemMeta();
            if (Objects.requireNonNull(itemMeta).hasDisplayName() || itemMeta.hasCustomModelData()) {
                return;
            }
        } else if (material == Material.END_PORTAL_FRAME) {
            player = e.getPlayer();

            if (player.getInventory().getItemInMainHand().getType() != Material.END_PORTAL_FRAME) {
                return;
            }
        } else {
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
        block.getWorld().dropItemNaturally(location, new ItemStack(material));
        player.playSound(location.add(0.5, 0.5, 0.5), block.getBlockData().getSoundGroup().getBreakSound(), 1.0F, 1.0F);
    }

    // 切换光源方块亮度等级
    @EventHandler
    public void onToggleLight(final @NotNull PlayerInteractEvent e) {
        final Action action = e.getAction();
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.LEFT_CLICK_AIR) {
            return;
        }

        final Player player = e.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.LIGHT) {
            return;
        }

        try {
            if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.LIGHT) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
        }

        final BlockDataMeta blockDataMeta = (BlockDataMeta) itemStack.getItemMeta();

        final Light light = (Light) Objects.requireNonNull(blockDataMeta).getBlockData(Material.LIGHT);
        light.setLevel((light.getLevel() + 1) % (light.getMaximumLevel() + 1));
        blockDataMeta.setBlockData(light);

        itemStack.setItemMeta(blockDataMeta);
    }
}
