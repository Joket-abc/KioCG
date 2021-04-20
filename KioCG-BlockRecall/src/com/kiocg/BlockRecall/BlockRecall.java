package com.kiocg.BlockRecall;

import org.bukkit.plugin.java.JavaPlugin;

public class BlockRecall extends JavaPlugin {
    @SuppressWarnings("unused")
    public static BlockRecall instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
