package com.kiocg.LittleThings;

import com.kiocg.LittleThings.listeners.Fun.boneMealPlayer;
import com.kiocg.LittleThings.listeners.Fun.creeperFirework;
import com.kiocg.LittleThings.listeners.Fun.dropItemSound;
import com.kiocg.LittleThings.listeners.Fun.skeletonPotion;
import com.kiocg.LittleThings.listeners.Misc.*;
import com.kiocg.LittleThings.listeners.Utility.*;
import com.kiocg.LittleThings.utility.AutoRestart;
import com.kiocg.LittleThings.utility.FullMoon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LittleThings extends JavaPlugin {
    public static LittleThings instance;

    @Override
    public void onEnable() {
        instance = this;

        final PluginManager pluginManager = getServer().getPluginManager();
        // Fun
        pluginManager.registerEvents(new boneMealPlayer(), this);
        pluginManager.registerEvents(new creeperFirework(), this);
        pluginManager.registerEvents(new dropItemSound(), this);
        pluginManager.registerEvents(new skeletonPotion(), this);
        // Misc
        pluginManager.registerEvents(new atPlayer(), this);
        pluginManager.registerEvents(new catchMonsters(), this);
        pluginManager.registerEvents(new compassTeleport(), this);
        pluginManager.registerEvents(new originRespawn(), this);
        pluginManager.registerEvents(new portableWorkbench(), this);
        pluginManager.registerEvents(new protectPlace(), this);
        pluginManager.registerEvents(new randomPlace(), this);
        // Utility
        pluginManager.registerEvents(new cancelDestroyByEntity(), this);
        pluginManager.registerEvents(new cancelSomeRename(), this);
        pluginManager.registerEvents(new cancelSomeSpawnerSpawn(), this);
        pluginManager.registerEvents(new fixVanishingCurse(), this);
        pluginManager.registerEvents(new limitSpawner(), this);

        // 启动定时自动重启
        new AutoRestart();

        // 满月特殊效果
        new FullMoon();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
