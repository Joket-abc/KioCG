package com.kiocg.ProtectSpawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ProtectSpawn extends @NotNull JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }
}
