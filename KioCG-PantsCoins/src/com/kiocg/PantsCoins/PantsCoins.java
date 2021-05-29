package com.kiocg.PantsCoins;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PantsCoins extends JavaPlugin {
    @SuppressWarnings("unused")
    public static PantsCoins instance;

    @Override
    public void onEnable() {
        instance = this;

        new BlockManager().setup();

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getServer().getPluginCommand("custommodeldata")).setExecutor(new CustomModelDataCommand());
    }
}
