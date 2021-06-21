package com.kiocg.ItemCompress;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemCompress extends JavaPlugin {
    public static ItemCompress instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        new Utils().loadConfig();
        new CreateRecipe();

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        getServer().resetRecipes();
    }
}
