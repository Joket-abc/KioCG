package com.kiocg.ProtectSpawn;

import org.bukkit.plugin.java.JavaPlugin;

public class ProtectSpawn extends JavaPlugin {
    @SuppressWarnings("unused")
    public static ProtectSpawn instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
