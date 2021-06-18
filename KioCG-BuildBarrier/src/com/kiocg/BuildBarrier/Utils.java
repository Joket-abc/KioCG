package com.kiocg.BuildBarrier;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Utils {
    @SuppressWarnings("ImplicitNumericConversion")
    public static void sendBarrierChange(final @NotNull Player player) {
        final Location location = player.getLocation();
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY();
        final int blockZ = location.getBlockZ();
        final World world = location.getWorld();

        for (int x = blockX - 32; x <= blockX + 32; ++x) {
            for (int y = blockY - 32; y <= blockY + 32; ++y) {
                for (int z = blockZ - 32; z <= blockZ + 32; ++z) {
                    final Location loc = new Location(world, x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.BARRIER) {
                        player.spawnParticle(Particle.BARRIER, loc.toCenterLocation(), 1);
                    }
                }
            }
        }
    }
}
