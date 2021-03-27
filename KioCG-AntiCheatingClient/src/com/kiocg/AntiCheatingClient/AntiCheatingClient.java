package com.kiocg.AntiCheatingClient;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AntiCheatingClient extends @NotNull JavaPlugin {
    public static AntiCheatingClient instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
