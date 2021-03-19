package com.kiocg.LittleThings;

import com.kiocg.LittleThings.fix.Fix;
import com.kiocg.LittleThings.fun.Fun;
import com.kiocg.LittleThings.misc.Misc;
import com.kiocg.LittleThings.utility.AutoRestart;
import com.kiocg.LittleThings.utility.Utility;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LittleThings extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName"})
    public static LittleThings INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

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
