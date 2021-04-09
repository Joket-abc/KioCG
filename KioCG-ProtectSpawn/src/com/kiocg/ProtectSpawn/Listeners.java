package com.kiocg.ProtectSpawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements @NotNull Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockBreak(final @NotNull BlockBreakEvent e) {
        final Player player = e.getPlayer();

        if (player.isOp()) {
            return;
        }

        final Location loc = e.getBlock().getLocation();
        if (!"KioCG_world".equals(loc.getWorld().getName()) || loc.distance(Utils.locSpawn) > 64.0) {
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockPlace(final @NotNull BlockPlaceEvent e) {
        final Player player = e.getPlayer();

        if (player.isOp()) {
            return;
        }

        final Location loc = e.getBlock().getLocation();
        if (!"KioCG_world".equals(loc.getWorld().getName()) || loc.distance(Utils.locSpawn) > 64.0) {
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §6接受惩罚吧~ 破坏主城的坏孩子!");
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction().equals(Action.PHYSICAL)) {
            final Block block = e.getClickedBlock();

            final Location loc = Objects.requireNonNull(block).getLocation();

            if (Objects.requireNonNull(block).getType().equals(Material.FARMLAND)
                && "KioCG_world".equals(loc.getWorld().getName()) && loc.distance(Utils.locSpawn) <= 64.0) {

                e.setCancelled(true);
            }
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            final Block block = e.getClickedBlock();
            final Location loc = Objects.requireNonNull(block).getLocation();

            final Player player = e.getPlayer();

            if (Objects.requireNonNull(block).getType().equals(Material.CAKE)
                && "KioCG_world".equals(loc.getWorld().getName()) && loc.distance(Utils.locSpawn) <= 64.0
                && player.getFoodLevel() != 20) {

                if (!Utils.eatCake.contains(player.getUniqueId())) {
                    for (final Player toPlayer : Bukkit.getOnlinePlayers()) {
                        toPlayer.sendMessage(player.getName() + "获得成就§a[蛋糕是个谎言]");
                    }

                    Utils.eatCake.add(player.getUniqueId());
                }

                player.damage(99.9);
                e.setCancelled(true);
            }
        }
    }
}
