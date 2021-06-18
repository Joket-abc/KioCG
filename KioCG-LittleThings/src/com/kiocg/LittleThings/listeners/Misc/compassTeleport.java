package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class compassTeleport implements Listener {
    // 磁石指针传送
    @EventHandler
    public void compassTeleport(final @NotNull PlayerInteractEvent e) {
        final Action action = e.getAction();
        if ((action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) || Objects.requireNonNull(e.getHand()) != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack itemStack = e.getItem();

        try {
            if (Objects.requireNonNull(itemStack).getType() != Material.COMPASS) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final Player player = e.getPlayer();
        try {
            player.teleport(player.getLocation().add(Objects.requireNonNull(((CompassMeta) itemStack.getItemMeta()).getLodestone()).toCenterLocation(), 0.0, 0.5, 0.0));
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 展示框磁石指针传送
    @EventHandler
    public void compassItemFrameTeleport(final @NotNull PlayerInteractEntityEvent e) {
        final Player player = e.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        final Entity entityClicked = e.getRightClicked();

        if (!(entityClicked instanceof ItemFrame)) {
            return;
        }

        final ItemStack itemStack = ((ItemFrame) entityClicked).getItem();

        if (itemStack.getType() != Material.COMPASS) {
            return;
        }

        try {
            player.teleport(player.getLocation().add(Objects.requireNonNull(((CompassMeta) itemStack.getItemMeta()).getLodestone()).toCenterLocation(), 0.0, 0.5, 0.0));
            e.setCancelled(true);
        } catch (final @NotNull NullPointerException ignore) {
        }
    }
}
