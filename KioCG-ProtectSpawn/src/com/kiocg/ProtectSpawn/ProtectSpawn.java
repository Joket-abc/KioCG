package com.kiocg.ProtectSpawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ProtectSpawn extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName", "unused"})
    public static ProtectSpawn INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
