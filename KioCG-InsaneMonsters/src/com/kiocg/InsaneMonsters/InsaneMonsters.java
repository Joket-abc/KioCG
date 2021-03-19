package com.kiocg.InsaneMonsters;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InsaneMonsters extends @NotNull JavaPlugin {
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "InsaneMonsters");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this, namespacedKey), this);
        Objects.requireNonNull(getServer().getPluginCommand("insanemonsters")).setExecutor(new IMCommand(namespacedKey));
    }
}
