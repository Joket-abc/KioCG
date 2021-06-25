package com.kiocg.LittleThings.listeners.Misc;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class portableWorkbench implements Listener {
    // 随身工作台
    @EventHandler(ignoreCancelled = true)
    public void portableWorkbench(final @NotNull InventoryClickEvent e) {
        try {
            if (Objects.requireNonNull(e.getClickedInventory()).getType() != InventoryType.CRAFTING) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (e.getSlotType() != InventoryType.SlotType.RESULT || Objects.requireNonNull(e.getCurrentItem()).getType() != Material.AIR) {
            return;
        }

        final Player player = (Player) e.getWhoClicked();

        if (player.hasPermission("kiocg.littlethings.fastworkbench") || player.getStatistic(Statistic.PLAY_ONE_MINUTE) > 20 * 60 * 60 * 24) {
            Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
                player.closeInventory();
                player.openWorkbench(player.getLocation(), true);
            });

            e.setCancelled(true);
        }
    }
}
