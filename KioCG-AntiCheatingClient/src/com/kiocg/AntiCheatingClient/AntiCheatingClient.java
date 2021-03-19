package com.kiocg.AntiCheatingClient;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AntiCheatingClient extends @NotNull JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }
}
