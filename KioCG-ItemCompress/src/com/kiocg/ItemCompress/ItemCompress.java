package com.kiocg.ItemCompress;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ItemCompress extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName", "unused"})
    public static ItemCompress INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        new CreateRecipe(this);
        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        getServer().resetRecipes();
    }
}
