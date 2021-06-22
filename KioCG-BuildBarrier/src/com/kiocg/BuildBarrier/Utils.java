package com.kiocg.BuildBarrier;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {
    // 存储开启观察模式的玩家、粒子发送任务
    public static final Map<Player, BukkitTask> particleTasks = new HashMap<>();

    public static void sendBarrierChange(final @NotNull Player player) {
        try {
            particleTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }

        particleTasks.put(player, sendParticleTask(player, Material.BARRIER, Particle.BARRIER));
    }

    public static void sendLightChange(final @NotNull Player player) {
        try {
            particleTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }

        //TODO 修改为光源方块粒子
        particleTasks.put(player, sendParticleTask(player, Material.LIGHT, Particle.BARRIER));
    }

    @SuppressWarnings("ImplicitNumericConversion")
    public static @NotNull BukkitTask sendParticleTask(final @NotNull Player player, final @NotNull Material material, final @NotNull Particle particle) {
        return new BukkitRunnable() {
            int i;

            @Override
            public void run() {
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

                if (++i > 5) {
                    cancel();
                }
            }
        }.runTaskTimer(BuildBarrier.instance, 0L, 60L);
    }
}
