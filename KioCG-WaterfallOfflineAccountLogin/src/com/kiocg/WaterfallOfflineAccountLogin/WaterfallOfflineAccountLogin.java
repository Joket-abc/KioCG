package com.kiocg.WaterfallOfflineAccountLogin;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class WaterfallOfflineAccountLogin extends Plugin {
    @SuppressWarnings("unused")
    public static WaterfallOfflineAccountLogin instance;

    @Override
    public void onEnable() {
        instance = this;

        BungeeCord.getInstance().getPluginManager().registerListener(this, new Listeners());
    }

    @Override
    public void onDisable() {
        BungeeCord.getInstance().getPluginManager().unregisterListeners(this);
    }
}
