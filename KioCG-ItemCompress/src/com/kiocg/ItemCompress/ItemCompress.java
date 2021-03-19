package com.kiocg.ItemCompress;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ItemCompress extends @NotNull JavaPlugin {
    @Override
    public void onEnable() {
        new CreateRecipe(this);
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        getServer().resetRecipes();
    }
}
