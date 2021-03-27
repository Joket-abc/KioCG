package com.kiocg.InfiniteEnchant;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfiniteEnchant extends @NotNull JavaPlugin {
    @SuppressWarnings("unused")
    public static InfiniteEnchant instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
