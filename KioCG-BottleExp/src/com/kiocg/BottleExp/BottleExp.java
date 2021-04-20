package com.kiocg.BottleExp;

import org.bukkit.plugin.java.JavaPlugin;

public class BottleExp extends JavaPlugin {
    public static BottleExp instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
