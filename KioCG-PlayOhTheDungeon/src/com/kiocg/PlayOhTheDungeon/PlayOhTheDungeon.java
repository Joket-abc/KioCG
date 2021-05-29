package com.kiocg.PlayOhTheDungeon;

import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

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

        for (final Player player : getServer().getOnlinePlayers()) {
            Utils.playerRabbits.put(player.getUniqueId().toString(), 0);
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getServer().getPluginCommand("rabbit")).setExecutor(new RabbitCommand());
    }
}
