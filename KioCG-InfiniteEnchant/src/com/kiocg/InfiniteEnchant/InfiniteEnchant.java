package com.kiocg.InfiniteEnchant;

import org.bukkit.plugin.java.JavaPlugin;

public class InfiniteEnchant extends JavaPlugin {
    @SuppressWarnings("unused")
    public static InfiniteEnchant instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
