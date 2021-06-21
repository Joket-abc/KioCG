package com.kiocg.InfiniteEnchant;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfiniteEnchant extends JavaPlugin {
    @SuppressWarnings("unused")
    public static InfiniteEnchant instance;

    @Override
    public void onEnable() {
        instance = this;

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Listeners(), this);

        try {
            Class.forName("com.destroystokyo.paper.event.inventory.PrepareResultEvent");

            pluginManager.registerEvents(new ListenersPaper(), this);
        } catch (final @NotNull ClassNotFoundException ignore) {
        }
    }
}
