package com.kiocg.LittleThings;

import com.kiocg.LittleThings.listeners.Fix;
import com.kiocg.LittleThings.listeners.Fun;
import com.kiocg.LittleThings.listeners.Misc;
import com.kiocg.LittleThings.listeners.Utility;
import com.kiocg.LittleThings.utility.AutoRestart;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LittleThings extends JavaPlugin {
    public static LittleThings instance;

    public static NamespacedKey namespacedKey;

    @Override
    public void onEnable() {
        instance = this;

        namespacedKey = new NamespacedKey(this, "SpawnBlockKey");

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Fix(), this);
        pluginManager.registerEvents(new Fun(), this);
        pluginManager.registerEvents(new Misc(), this);
        pluginManager.registerEvents(new Utility(), this);

        // 启动定时自动重启
        new AutoRestart();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
