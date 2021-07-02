package com.kiocg.ShopItemOnly;

import org.bukkit.plugin.java.JavaPlugin;

public class ShopItemOnly extends JavaPlugin {
    @SuppressWarnings("unused")
    public static ShopItemOnly instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
