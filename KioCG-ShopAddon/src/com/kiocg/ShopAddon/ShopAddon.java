package com.kiocg.ShopAddon;

import com.kiocg.ShopAddon.Listeners.LimitDailySales;
import com.kiocg.ShopAddon.Listeners.ShopItemOnly;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopAddon extends JavaPlugin {
    @SuppressWarnings("unused")
    public static ShopAddon instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new ShopItemOnly(), this);
        getServer().getPluginManager().registerEvents(new LimitDailySales(), this);
    }
}
