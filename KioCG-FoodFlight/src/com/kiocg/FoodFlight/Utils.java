package com.kiocg.FoodFlight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utils {
    // 存储所有开启飞行的玩家
    public static final Set<Player> flightPlayers = new HashSet<>();
    // 存储开启消耗饱食度飞行的玩家、扣除饱食度任务
    public static final Map<Player, BukkitTask> foodFlightTasks = new HashMap<>();

    // 定期检查飞行玩家权限的改变
    public void checkPermTask() {
        Bukkit.getScheduler().runTaskTimer(FoodFlight.instance, () -> {
            for (final Player player : Utils.flightPlayers) {
                final String worldName = player.getWorld().getName().toLowerCase();

                if (player.hasPermission("kiocg.foodflight.free." + worldName)) {
                    if (Utils.isFoodFlight(player)) {
                        Utils.removeFoodFlight(player);
                        player.sendMessage("§a[§b豆渣子§a] §2➹ 飞行状态已改变, 正在无限飞行 ➹");
                    }
                    return;
                }

                if (player.hasPermission("kiocg.foodflight." + worldName)) {
                    if (!Utils.isFoodFlight(player)) {
                        Utils.startFoodFlightTask(player);
                        player.sendMessage("§a[§b豆渣子§a] §2➹ 飞行状态已改变, 正在饥饿飞行 ➹");
                    }
                    return;
                }

                Utils.removeFlightList(player);
                player.sendMessage("§a[§b豆渣子§a] §c➷ 警告, 无法维持飞行升力 ➷");
            }
        }, 20L * 60L, 20L * 60L);
    }

    private static @NotNull BukkitTask flightTask(final @NotNull Player player) {
        return new BukkitRunnable() {
            int i;

            @Override
            public void run() {
                i = 1 - i;

                if (i == 1 || player.isSprinting()) {
                    player.setExhaustion(5.0F);
                }

                final Location location = player.getLocation();

                // 播放烟雾动画来区分可能作弊的玩家
                final World world = player.getWorld();
                world.spawnParticle(Particle.ASH, location, 9);

                if (player.getFoodLevel() <= 6) {
                    player.setFlying(false);

                    // 不使用this.stopFoodFlightTask(Player), 以免任务在意外情况下无法被取消
                    foodFlightTasks.put(player, null);
                    cancel();
                    return;
                }

                final int BlockY = location.getBlockY();
                if ((world.getEnvironment() == World.Environment.NORMAL && BlockY < 48) || BlockY > 128) {
                    player.setFlying(false);

                    player.sendMessage("§a[§b豆渣子§a] §c➷ 这里的空气太稀薄了 ➷");

                    // 不使用this.stopFoodFlightTask(Player), 以免任务在意外情况下无法被取消
                    foodFlightTasks.put(player, null);
                    cancel();
                }
            }
        }.runTaskTimer(FoodFlight.instance, 0L, 5L);
    }

    public static boolean inFlightList(final @NotNull Player player) {
        return flightPlayers.contains(player);
    }

    public static boolean isFoodFlight(final @NotNull Player player) {
        return foodFlightTasks.containsKey(player);
    }

    public static void addFlightList(final @NotNull Player player, final boolean isFoodFlight) {
        flightPlayers.add(player);

        if (isFoodFlight) {
            foodFlightTasks.put(player, null);
        }
    }

    public static void startFoodFlightTask(final @NotNull Player player) {
        foodFlightTasks.put(player, flightTask(player));
    }

    public static void stopFoodFlightTask(final @NotNull Player player) {
        try {
            foodFlightTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }

        foodFlightTasks.put(player, null);
    }

    public static void removeFlightList(final @NotNull Player player) {
        flightPlayers.remove(player);
        removeFoodFlight(player);

        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public static void removeFoodFlight(final @NotNull Player player) {
        try {
            foodFlightTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }

        foodFlightTasks.remove(player);
    }
}
