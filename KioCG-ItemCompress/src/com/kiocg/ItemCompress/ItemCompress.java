package com.kiocg.ItemCompress;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemCompress extends JavaPlugin {
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
