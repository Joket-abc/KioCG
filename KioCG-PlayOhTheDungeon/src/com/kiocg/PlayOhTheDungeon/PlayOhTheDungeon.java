package com.kiocg.PlayOhTheDungeon;

import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlayOhTheDungeon extends JavaPlugin {
    public static PlayOhTheDungeon instance;

    @Override
    public void onEnable() {
        instance = this;

        final File dataFolder = new File(getDataFolder(), "");
        if (!dataFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }

        if (getServer().createWorld(new WorldCreator("KioCG_OhTheDungeon")) == null) {
            getLogger().warning("地牢世界 KioCG_OhTheDungeon 加载失败!");
            return;
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        for (final Player player : getServer().getOnlinePlayers()) {
            Utils.playerRabbits.put(player.getUniqueId().toString(), 0);
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);

        for (final Player player : getServer().getOnlinePlayers()) {
            if ("KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
                player.setHealth(0.0);
                player.sendMessage("§a[§b豆渣子§a] §2你醒了... 可是为什么你在这?");
                Utils.restoreBackpackAndLoot(player);
            }
        }
    }
}
