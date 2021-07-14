package com.kiocg.ProtectSpawn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockBreak(final @NotNull BlockBreakEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockPlace(final @NotNull BlockPlaceEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelEntityPlace(final @NotNull EntityPlaceEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (Objects.requireNonNull(player).hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelHangingPlace(final @NotNull HangingPlaceEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (Objects.requireNonNull(player).hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerBucketEmpty(final @NotNull PlayerBucketEmptyEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerBucketFill(final @NotNull PlayerBucketFillEvent e) {
        if (!Utils.inSpawn(e.getBlock().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerBucketEntity(final @NotNull PlayerBucketEntityEvent e) {
        if (!Utils.inSpawn(e.getEntity().getLocation())) {
            return;
        }

        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        player.damage(1.0);
        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            final Block block = e.getClickedBlock();

            if (Objects.requireNonNull(block).getType() != Material.FARMLAND) {
                return;
            }

            if (!Utils.inSpawn(block.getLocation())) {
                return;
            }

            e.setCancelled(true);
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block block = e.getClickedBlock();

            if (!Utils.inSpawn(Objects.requireNonNull(block).getLocation())) {
                return;
            }

            final Player player = e.getPlayer();

            if (player.hasPermission("kiocg.protectspawn.bypass")) {
                return;
            }

            // 蛋糕是个谎言
            final Material material = block.getType();
            if ((material == Material.CAKE || material.toString().endsWith("_CAKE")) && player.getFoodLevel() != 20) {
                final String uuidString = player.getUniqueId().toString();
                if (!Utils.eatCake.contains(uuidString)) {
                    Bukkit.getOnlinePlayers().forEach(toPlayer -> toPlayer.sendMessage(player.getName() + "获得成就§a[蛋糕是个谎言]"));
                    Utils.eatCake.add(uuidString);
                }

                player.setHealth(0.0);

                e.setCancelled(true);
                return;
            }

            if (block.getType() == Material.ANVIL) {
                e.setCancelled(true);
                return;
            }

            if (block.getState() instanceof TileState) {
                switch (block.getType()) {
                    case ENDER_CHEST:
                    case ENCHANTING_TABLE:
                    case BELL:
                        return;
                }

                e.setCancelled(true);
                return;
            }

            final BlockData blockData = block.getBlockData();
            if (blockData instanceof TrapDoor || blockData instanceof Bed) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Monster || entity instanceof Phantom) {
            return;
        }

        if (!Utils.inSpawn(entity.getLocation())) {
            return;
        }

        final Entity damager = e.getDamager();
        if (!(damager instanceof Player) || damager.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelHangingBreakByEntity(final @NotNull HangingBreakByEntityEvent e) {
        if (!Utils.inSpawn(e.getEntity().getLocation())) {
            return;
        }

        final Entity remover = e.getRemover();
        if (!(remover instanceof Player) || remover.hasPermission("kiocg.protectspawn.bypass")) {
            return;
        }

        e.setCancelled(true);
    }

    // 取消实体爆炸
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelEntityExplode(final @NotNull EntityExplodeEvent e) {
        if (Utils.inSpawn(e.getLocation())) {
            e.setCancelled(true);
        }
    }
}
