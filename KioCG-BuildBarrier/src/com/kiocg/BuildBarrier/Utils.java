package com.kiocg.BuildBarrier;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {
    // 存储开启观察模式的玩家、粒子发送任务
    public static final Map<Player, BukkitTask> particleTasks = new HashMap<>();

    public static void sandBarrierTask(final @NotNull Player player) {
        cancelParticleTask(player);

        particleTasks.put(player, sendParticleTask(player, Material.BARRIER, Particle.BARRIER));
    }

    public static void sandLightTask(final @NotNull Player player) {
        cancelParticleTask(player);

        particleTasks.put(player, sendParticleTask(player, Material.LIGHT, Particle.LIGHT));
    }

    public static void cancelParticleTask(final @NotNull Player player) {
        try {
            particleTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    @SuppressWarnings("ImplicitNumericConversion")
    private static @NotNull BukkitTask sendParticleTask(final @NotNull Player player, final @NotNull Material material, final @NotNull Particle particle) {
        return Bukkit.getScheduler().runTaskTimer(BuildBarrier.instance, () -> {
            final Location location = player.getLocation();
            final int blockX = location.getBlockX();
            final int blockY = location.getBlockY();
            final int blockZ = location.getBlockZ();
            final World world = location.getWorld();

            for (int x = blockX - 32; x <= blockX + 32; ++x) {
                for (int y = blockY - 32; y <= blockY + 32; ++y) {
                    for (int z = blockZ - 32; z <= blockZ + 32; ++z) {
                        final Location loc = new Location(world, x, y, z);
                        if (Objects.requireNonNull(world).getBlockAt(loc).getType() == material) {
                            player.spawnParticle(particle, loc.add(0.5, 0.5, 0.5), 1);
                        }
                    }
                }
            }
        }, 0L, 60L);
    }
}
