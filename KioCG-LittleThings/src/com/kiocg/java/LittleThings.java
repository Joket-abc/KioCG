package com.kiocg.java;

import com.kiocg.java.fix.Fix;
import com.kiocg.java.fun.Fun;
import com.kiocg.java.misc.Misc;
import com.kiocg.java.utility.AutoRestart;
import com.kiocg.java.utility.Utility;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LittleThings extends JavaPlugin {
    private static LittleThings instance;

    public static LittleThings getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
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
