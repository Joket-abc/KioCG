package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Location;
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
    // 磁石指针用法提示
    @EventHandler
    public void compassTeleport(final @NotNull PlayerInteractEvent e) {
        final Action action = e.getAction();
        if ((action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) || Objects.requireNonNull(e.getHand()) != EquipmentSlot.HAND) {
            return;
        }

        try {
            if (Objects.requireNonNull(e.getItem()).getType() != Material.COMPASS) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        try {
            if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.LODESTONE) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
        }

        e.getPlayer().sendMessage("§a[§b豆渣子§a] §6请将磁石指针放入展示框中, 潜行对其右键即可快速传送.");
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

        final CompassMeta compassMeta = (CompassMeta) Objects.requireNonNull(itemStack.getItemMeta());
        final Location tpLocation = compassMeta.getLodestone();

        if (tpLocation == null) {
            return;
        }

        e.setCancelled(true);

        if (tpLocation.getBlock().getType() != Material.LODESTONE) {
            compassMeta.setLodestoneTracked(false);
            itemStack.setItemMeta(compassMeta);
            return;
        }

        Objects.requireNonNull(tpLocation).setDirection(player.getLocation().getDirection());
        player.teleport(tpLocation.add(0.5, 1.0, 0.5));
    }
}
