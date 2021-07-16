package com.kiocg.AntiAutoFish;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiAutoFish extends JavaPlugin {
    @SuppressWarnings("unused")
    public static AntiAutoFish instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
