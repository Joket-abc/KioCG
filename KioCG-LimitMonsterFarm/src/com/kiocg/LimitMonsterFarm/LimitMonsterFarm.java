package com.kiocg.LimitMonsterFarm;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class LimitMonsterFarm extends JavaPlugin {
    @SuppressWarnings("unused")
    public static LimitMonsterFarm instance;

    public static NamespacedKey namespacedKey;

    @Override
    public void onEnable() {
        instance = this;

        namespacedKey = new NamespacedKey(this, "SpawnBlockKey");

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }
}
