package com.kiocg.FoodFlight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FoodFlight extends JavaPlugin implements Listener {
    private static FoodFlight instance;

    public static FoodFlight getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getServer().getPluginCommand("fly")).setExecutor(new FlyCommand());
    }

    @Override
    public void onDisable() {
        for (final Player player : Utils.flightPlayers) {
            new Utils().removeFoodFlight(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("§7[§b豆渣子§7] §c插件重载迫使你关闭了飞行模式.");
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (Utils.inFlightList(player)) {
            new Utils().removeFlightList(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent e) {
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
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();
        if (!Utils.inFlightList(player)) {
            return;
        }

        switch (player.getWorld().getEnvironment()) {
            case NORMAL:
                if (player.hasPermission("kiocg.foodflight.normal.free")) {
                    if (Utils.isFoodFlight(player)) {
                        new Utils().removeFoodFlight(player);
                    }
                    return;
                } else if (player.hasPermission("kiocg.foodflight.normal")) {
                    if (!Utils.isFoodFlight(player)) {
                        Utils.startFoodFlightTask(player);
                    }
                    return;
                }
                break;
            case NETHER:
                if (player.hasPermission("kiocg.foodflight.nether.free")) {
                    if (Utils.isFoodFlight(player)) {
                        new Utils().removeFoodFlight(player);
                    }
                    return;
                } else if (player.hasPermission("kiocg.foodflight.nether")) {
                    if (!Utils.isFoodFlight(player)) {
                        Utils.startFoodFlightTask(player);
                    }
                    return;
                }
                break;
            case THE_END:
                if (player.hasPermission("kiocg.foodflight.end.free")) {
                    if (Utils.isFoodFlight(player)) {
                        new Utils().removeFoodFlight(player);
                    }
                    return;
                } else if (player.hasPermission("kiocg.foodflight.end")) {
                    if (!Utils.isFoodFlight(player)) {
                        Utils.startFoodFlightTask(player);
                    }
                    return;
                }
                break;
        }

        new Utils().removeFlightList(player);
        player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
    }
}
