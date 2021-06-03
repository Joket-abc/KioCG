package com.kiocg.OfflineAccountLogin;

import org.bukkit.plugin.java.JavaPlugin;

public class OfflineAccountLogin extends JavaPlugin {
    @SuppressWarnings("unused")
    public static OfflineAccountLogin instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
