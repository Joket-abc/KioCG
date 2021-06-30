package com.kiocg.LittleThings;

import com.kiocg.LittleThings.listeners.Fun.*;
import com.kiocg.LittleThings.listeners.Misc.*;
import com.kiocg.LittleThings.listeners.Utility.*;
import com.kiocg.LittleThings.scheduler.AutoRestart;
import com.kiocg.LittleThings.scheduler.FullMoon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LittleThings extends JavaPlugin {
    public static LittleThings instance;

    @Override
    public void onEnable() {
        instance = this;

        final PluginManager pluginManager = getServer().getPluginManager();
        // Fun
        pluginManager.registerEvents(new boneMealPlayer(), this);
        pluginManager.registerEvents(new creeperFirework(), this);
        pluginManager.registerEvents(new deathPenalty(), this);
        pluginManager.registerEvents(new dropItemSound(), this);
        pluginManager.registerEvents(new skeletonPotion(), this);
        // Misc
        pluginManager.registerEvents(new atPlayer(), this);
        pluginManager.registerEvents(new catchAnimals(), this);
        pluginManager.registerEvents(new compassTeleport(), this);
        pluginManager.registerEvents(new copySign(), this);
        pluginManager.registerEvents(new portableWorkbench(), this);
        pluginManager.registerEvents(new protectPlace(), this);
        pluginManager.registerEvents(new randomPlace(), this);
        // Utility
        pluginManager.registerEvents(new cancelDestroyByEntity(), this);
        pluginManager.registerEvents(new cancelSomeRename(), this);
        pluginManager.registerEvents(new cancelSpawnerLegacy(), this);
        pluginManager.registerEvents(new fixLureEnchanting(), this);
        pluginManager.registerEvents(new fixVanishingCurse(), this);

        // 启动定时自动重启
        new AutoRestart();

        try {
            Class.forName("io.papermc.paper.world.MoonPhase");

            // 满月特殊效果
            new FullMoon();
        } catch (final @NotNull ClassNotFoundException ignore) {
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
