package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class limitSpawner implements Listener {
    // 限制刷怪笼
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void limitSpawner(final @NotNull BlockPlaceEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.spawnerlimit.bypass")) {
            return;
        }

        final Block block = e.getBlockPlaced();
        if (block.getType() != Material.SPAWNER) {
            return;
        }

        final Chunk chunk = block.getChunk();
        final int chunkX = chunk.getX();
        final int chunkZ = chunk.getZ();

        final World world = block.getWorld();

        int count = 1;
        for (int x = chunkX - 1; x <= chunkX + 1; ++x) {
            for (int z = chunkZ - 1; z <= chunkZ + 1; ++z) {
                for (final BlockState tileEntityState : world.getChunkAt(x, z).getTileEntities()) {
                    if (tileEntityState.getType() == Material.SPAWNER) {
                        if (++count > 16) {
                            player.sendMessage("§a[§b豆渣子§a] §6相邻3x3个区块内最多允许存在16个克隆箱.");

                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
