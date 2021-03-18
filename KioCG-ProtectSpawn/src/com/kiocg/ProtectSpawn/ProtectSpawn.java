package com.kiocg.ProtectSpawn;

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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ProtectSpawn extends JavaPlugin implements Listener {
    // 存储主城原点坐标
    private Location locSpawn;
    // 蛋糕是个谎言
    private final List<UUID> eatCake = new ArrayList<>();

    @Override
    public void onEnable() {
        locSpawn = new Location(getServer().getWorld("KioCG_world"), 187.5, 144.0, 209.5);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockBreak(final @NotNull BlockBreakEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }

        if (inSpawn(e.getBlock().getLocation())) {
            player.sendMessage("§7[§9豆渣子§7] §6接受惩罚吧~ 破坏主城的坏孩子!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelBlockPlace(final @NotNull BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }

        if (inSpawn(e.getBlock().getLocation())) {
            player.sendMessage("§7[§9豆渣子§7] §6接受惩罚吧~ 破坏主城的坏孩子!");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction().equals(Action.PHYSICAL)) {
            final Block block = e.getClickedBlock();
            if (Objects.requireNonNull(block).getType().equals(Material.FARMLAND) && inSpawn(block.getLocation())) {
                e.setCancelled(true);
            }
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            final Block block = e.getClickedBlock();
            final Player player = e.getPlayer();
            if (Objects.requireNonNull(block).getType().equals(Material.CAKE) && inSpawn(block.getLocation()) && player.getFoodLevel() != 20) {
                player.damage(99.9);
                e.setCancelled(true);
                if (!eatCake.contains(player.getUniqueId())) {
                    for (final Player toPlayer : getServer().getOnlinePlayers()) {
                        toPlayer.sendMessage(player.getName() + "获得成就§a[蛋糕是个谎言]");
                    }
                    eatCake.add(player.getUniqueId());
                }
            }
        }
    }

    private boolean inSpawn(final @NotNull Location loc) {
        if (!"KioCG_world".equals(loc.getWorld().getName())) {
            return false;
        }
        return loc.distance(locSpawn) <= 64.0;
    }
}
