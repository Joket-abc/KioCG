package com.kiocg.LittleThings.listeners.Fun;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class dropItemSound implements Listener {
    // 丢弃物品的音效
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void dropItemSound(final @NotNull PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 0.5F, 2.0F);
    }
}
