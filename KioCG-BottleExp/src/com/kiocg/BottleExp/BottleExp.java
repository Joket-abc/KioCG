package com.kiocg.BottleExp;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BottleExp extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName"})
    public static BottleExp INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
