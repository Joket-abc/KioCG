package com.kiocg.BlockRecall;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BlockRecall extends @NotNull JavaPlugin {
    @SuppressWarnings("unused")
    public static BlockRecall instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
