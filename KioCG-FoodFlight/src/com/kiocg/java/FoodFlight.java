package com.kiocg.java;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FoodFlight extends JavaPlugin implements Listener {
    // 存储开启消耗饱食度飞行的玩家、扣除饱食度任务
    public static final Map<Player, BukkitTask> flightPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getServer().getPluginCommand("fly")).setExecutor(new com.kiocg.java.Command(this));
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        for (final Player player : flightPlayers.keySet()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("§7[§b豆渣子§7] §c插件重载迫使你关闭了飞行模式.");
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (flightPlayers.containsKey(player)) {
            try {
                flightPlayers.get(player).cancel();
            } catch (final NullPointerException ignore) {
            }
            flightPlayers.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();
        if (!flightPlayers.containsKey(player)) {
            return;
        }

        switch (player.getWorld().getEnvironment()) {
            case NORMAL:
                if (player.hasPermission("kiocg.foodflight.normal") || player.hasPermission("kiocg.foodflight.normal.free")) {
                    return;
                }
                break;
            case NETHER:
                if (player.hasPermission("kiocg.foodflight.nether") || player.hasPermission("kiocg.foodflight.nether.free")) {
                    return;
                }
                break;
            case THE_END:
                if (player.hasPermission("kiocg.foodflight.end") || player.hasPermission("kiocg.foodflight.end.free")) {
                    return;
                }
                break;
        }

        try {
            flightPlayers.get(player).cancel();
        } catch (final NullPointerException ignore) {
        }
        flightPlayers.remove(player);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
    }
}
