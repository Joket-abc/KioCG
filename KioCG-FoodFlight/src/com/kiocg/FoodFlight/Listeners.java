package com.kiocg.FoodFlight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (Utils.inFlightList(player)) {
            Utils.removeFlightList(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleFlight(final @NotNull PlayerToggleFlightEvent e) {
        final Player player = e.getPlayer();

        if (!Utils.isFoodFlight(player)) {
            return;
        }

        if (e.isFlying()) {
            if (player.getFoodLevel() <= 6) {
                e.setCancelled(true);
                return;
            }

            Utils.startFoodFlightTask(player);
        } else {
            Utils.stopFoodFlightTask(player);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(final @NotNull PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();

        if (!Utils.inFlightList(player)) {
            return;
        }

        final String worldName = player.getWorld().getName().toLowerCase();

        if (player.hasPermission("kiocg.foodflight.free." + worldName)) {
            if (Utils.isFoodFlight(player)) {
                Utils.removeFoodFlight(player);
                player.sendMessage("§a[§b豆渣子§a] §2➹ 在这个世界可以无限飞行 ➹");
            }
            return;
        }

        if (player.hasPermission("kiocg.foodflight." + worldName)) {
            if (!Utils.isFoodFlight(player)) {
                Utils.startFoodFlightTask(player);
                player.sendMessage("§a[§b豆渣子§a] §2➹ 在这个世界需要饥饿飞行 ➹");
            }
            return;
        }

        Utils.removeFlightList(player);
        player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
    }
}
