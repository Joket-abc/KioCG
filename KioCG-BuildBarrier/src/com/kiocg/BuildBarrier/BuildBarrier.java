package com.kiocg.BuildBarrier;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BuildBarrier extends JavaPlugin {
    @SuppressWarnings("unused")
    public static BuildBarrier instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        Objects.requireNonNull(getServer().getPluginCommand("barrier")).setExecutor(new BarrierCommand());
    }
}
