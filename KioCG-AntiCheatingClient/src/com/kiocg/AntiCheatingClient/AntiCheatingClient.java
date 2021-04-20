package com.kiocg.AntiCheatingClient;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheatingClient extends JavaPlugin {
    public static AntiCheatingClient instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
