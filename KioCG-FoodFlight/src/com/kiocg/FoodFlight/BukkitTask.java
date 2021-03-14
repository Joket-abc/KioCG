package com.kiocg.FoodFlight;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BukkitTask {
    public @NotNull org.bukkit.scheduler.BukkitTask flightBukkitTask(final @NotNull FoodFlight foodFlight, final @NotNull Player player) {
        return new BukkitRunnable() {
            int i = 1;

            @Override
            public void run() {
                if (!player.isFlying()) {
                    i = 1;
                    return;
                }

                if (player.getFoodLevel() <= 6) {
                    player.setFlying(false);
                    i = 1;
                    return;
                }

                i = 1 - i;
                if (i == 0 || player.isSprinting()) {
                    player.setExhaustion(5.0F);
                }

                // 播放烟雾动画来区分可能作弊的玩家
                player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
            }
        }.runTaskTimer(foodFlight, 0L, 5L);
    }
}
