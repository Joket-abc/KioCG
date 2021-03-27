package com.kiocg.ProtectSpawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ProtectSpawn extends @NotNull JavaPlugin {
    @SuppressWarnings("unused")
    public static ProtectSpawn instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
