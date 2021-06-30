package com.kiocg.BuildBarrier;

import org.bukkit.plugin.java.JavaPlugin;

public class BuildBarrier extends JavaPlugin {
    @SuppressWarnings("unused")
    public static BuildBarrier instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
