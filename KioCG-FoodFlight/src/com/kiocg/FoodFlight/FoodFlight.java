package com.kiocg.FoodFlight;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FoodFlight extends @NotNull JavaPlugin {
    private static FoodFlight instance;

    public static FoodFlight getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new Events(), this);
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
}
