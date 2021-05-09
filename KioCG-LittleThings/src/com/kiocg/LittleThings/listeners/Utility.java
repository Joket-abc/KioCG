package com.kiocg.LittleThings.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class Utility implements Listener {
    // 防止重命名成内部保留的物品前缀名
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            return;
        }

        final String renameText = e.getInventory().getRenameText();

        if (Pattern.matches("^(&[0-9a-zA-Z]){3}.*$", renameText) || Pattern.matches("^(&#[0-9a-zA-Z]{6}){3}.*$", renameText)) {
            e.setResult(null);
        }
    }

    // 无法放置
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        try {
            for (final Component lore : Objects.requireNonNull(e.getItemInHand().lore())) {
                if (lore.toString().contains("无法放置")) {
                    e.setCancelled(true);
                    return;
                }
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 限制刷怪笼
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerPlace(final @NotNull BlockPlaceEvent e) {
        final Block block = e.getBlockPlaced();
        if (!block.getType().equals(Material.SPAWNER)) {
            return;
        }

        final Chunk chunk = block.getChunk();
        final int chunkX = chunk.getX();
        final int chunkZ = chunk.getZ();

        final World world = block.getWorld();
        int count = 1;
        for (int x = chunkX - 1; x <= chunkX + 1; ++x) {
            for (int z = chunkZ - 1; z <= chunkZ + 1; ++z) {
                for (final BlockState entityBlock : world.getChunkAt(x, z).getTileEntities()) {
                    if (entityBlock.getType().equals(Material.SPAWNER)) {
                        if (++count > 16) {
                            e.getPlayer().sendMessage("§a[§b豆渣子§a] §6相邻九个区块内最多允许存在16个克隆箱.");

                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    // 限制刷怪笼(临时的)
    // TODO 1.17移除
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerSpawn(final @NotNull SpawnerSpawnEvent e) {
        final Entity entity = e.getEntity();

        final Chunk chunk = entity.getChunk();
        final int chunkX = chunk.getX();
        final int chunkZ = chunk.getZ();

        final World world = entity.getWorld();
        int count = 1;
        for (int x = chunkX - 1; x <= chunkX + 1; ++x) {
            for (int z = chunkZ - 1; z <= chunkZ + 1; ++z) {
                for (final BlockState entityBlock : world.getChunkAt(x, z).getTileEntities()) {
                    if (entityBlock.getType().equals(Material.SPAWNER)) {
                        if (++count > 16) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
