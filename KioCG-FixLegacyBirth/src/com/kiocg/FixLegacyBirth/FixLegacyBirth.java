package com.kiocg.FixLegacyBirth;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class FixLegacyBirth extends JavaPlugin implements Listener {
    private final Set<String> fixed = new HashSet<>();

    @Override
    public void onEnable() {
        if (getServer().createWorld(new WorldCreator("KioCG_world")) == null) {
            getLogger().warning("旧版本世界 KioCG_world 加载失败!");
            return;
        }

        saveDefaultConfig();

        fixed.addAll(getConfig().getStringList("fixed"));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final String uuidString = player.getUniqueId().toString();

        if (!fixed.contains(uuidString)) {
            fixed.add(uuidString);
            getConfig().set("fixed", fixed);
            saveConfig();

            if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) < 20) {
                return;
            }

            final Location location = player.getLocation();
            location.setWorld(Bukkit.getWorld("KioCG_world"));
            player.teleport(location);
        }
    }
}
