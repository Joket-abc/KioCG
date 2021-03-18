package com.kiocg.BottleExp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BottleExp extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !Objects.requireNonNull(e.getHand()).equals(EquipmentSlot.HAND)) {
            return;
        }
        if (!Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.ENCHANTING_TABLE)) {
            return;
        }
        final ItemStack itemStack = e.getItem();
        try {
            if (!Objects.requireNonNull(itemStack).getType().equals(Material.GLASS_BOTTLE)) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }
        final Player player = e.getPlayer();
        if (!player.hasPermission("kiocg.bottleexp.use")) {
            return;
        }
        final int currentTotalExperience = new Utils().getCurrentTotalExperience(player);
        if (currentTotalExperience < 10) {
            return;
        }

        final World world = player.getWorld();
        final Location location = player.getLocation();
        if (player.isSneaking()) {
            final int amount = itemStack.getAmount();
            if (currentTotalExperience >= amount * 10) {
                player.giveExp(-amount * 10);
                world.dropItem(location, new ItemStack(Material.EXPERIENCE_BOTTLE, amount));
                itemStack.setAmount(0);
            } else {
                final int enough = currentTotalExperience / 10;
                player.giveExp(-enough * 10);
                world.dropItem(location, new ItemStack(Material.EXPERIENCE_BOTTLE, enough));
                itemStack.setAmount(amount - enough);
            }
            getServer().getScheduler().runTask(this, (Runnable) player::closeInventory);
        } else {
            player.giveExp(-10);
            world.dropItem(location, new ItemStack(Material.EXPERIENCE_BOTTLE));
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        world.playSound(location, Sound.BLOCK_BREWING_STAND_BREW, 1.0F, 1.0F);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExpBottle(final @NotNull ExpBottleEvent e) {
        e.setExperience(10);
    }
}
