package com.kiocg.LittleThings.listeners.Misc;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class protectPlace implements Listener {
    // 无法放置
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void protectPlace(final @NotNull BlockPlaceEvent e) {
        try {
            for (final Component lore : Objects.requireNonNull(e.getItemInHand().lore())) {
                if (PaperComponents.plainSerializer().serialize(lore).contains("无法放置")) {
                    e.setCancelled(true);
                    return;
                }
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }
}
