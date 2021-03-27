package com.kiocg.ItemCompress;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ItemCompress extends @NotNull JavaPlugin {
    @SuppressWarnings("unused")
    public static ItemCompress instance;

    @Override
    public void onEnable() {
        instance = this;

        new CreateRecipe(this);

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        getServer().resetRecipes();
    }
}
