package com.kiocg.test;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class test extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerFish(final PlayerFishEvent e) {
        final Player player = e.getPlayer();

        if (!player.isOp()) {
            return;
        }

        player.sendMessage(e.getState().toString());
    }
}
