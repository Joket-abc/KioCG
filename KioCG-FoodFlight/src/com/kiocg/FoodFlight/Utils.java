package com.kiocg.FoodFlight;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    // 存储所有开启飞行的玩家
    public static final List<Player> flightPlayers = new ArrayList<>();
    // 存储开启消耗饱食度飞行的玩家、扣除饱食度任务
    public static final Map<Player, BukkitTask> foodFlightTasks = new HashMap<>();

    private static @NotNull BukkitTask flightTask(final @NotNull Player player) {
        return new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i = 1 - i;
                if (i == 1 || player.isSprinting()) {
                    player.setExhaustion(5.0F);
                }

                // 播放烟雾动画来区分可能作弊的玩家
                player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 9);

                if (player.getFoodLevel() <= 6) {
                    player.setFlying(false);
                    // 不使用stopFoodFlightTask, 以免任务在意外情况下无法被取消
                    cancel();
                    foodFlightTasks.put(player, null);
                }
            }
        }.runTaskTimer(FoodFlight.getInstance(), 0L, 5L);
    }

    public static boolean inFlightList(final @NotNull Player player) {
        return flightPlayers.contains(player);
    }

    public static boolean isFoodFlight(final @NotNull Player player) {
        return foodFlightTasks.containsKey(player);
    }

    public void addFlightList(final @NotNull Player player, final boolean isFoodFlight) {
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

    public void removeFoodFlight(final @NotNull Player player) {
        try {
            foodFlightTasks.get(player).cancel();
        } catch (final @NotNull NullPointerException ignore) {
        }
        foodFlightTasks.remove(player);
    }

    public void removeFlightList(final @NotNull Player player) {
        flightPlayers.remove(player);
        if (isFoodFlight(player)) {
            removeFoodFlight(player);
        }
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
