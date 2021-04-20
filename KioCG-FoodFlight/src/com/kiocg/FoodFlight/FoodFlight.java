package com.kiocg.FoodFlight;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FoodFlight extends JavaPlugin {
    public static FoodFlight instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        Objects.requireNonNull(getServer().getPluginCommand("fly")).setExecutor(new FlyCommand());
    }

    @Override
    public void onDisable() {
        for (final Player player : Utils.flightPlayers) {
            Utils.removeFoodFlight(player);

            player.setAllowFlight(false);
            player.setFlying(false);

            player.sendMessage("§a[§b豆渣子§a] §c插件重载迫使你关闭了飞行模式.");
        }
    }
}
