package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

        final Player player = e.getPlayer();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.COMPASS) {
            return;
        }

        try {
            player.teleport(Objects.requireNonNull(((CompassMeta) itemStack.getItemMeta()).getLodestone()).toCenterLocation().add(0.0, 1.0, 0.0));
        } catch (final @NotNull NullPointerException ignore) {
        }
    }
}
