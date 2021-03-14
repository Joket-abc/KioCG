package com.kiocg.BottleExp;

import org.bukkit.Material;
import org.bukkit.Sound;
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
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !Objects.equals(e.getHand(), EquipmentSlot.HAND)) {
            return;
        }
        if (!Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.ENCHANTING_TABLE)) {
            return;
        }
        final Player player = e.getPlayer();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }
        final int totalExperience = new Utils().getTotalExperience(player);
        if (totalExperience < 10) {
            return;
        }

        if (player.isSneaking()) {
            if (totalExperience >= itemStack.getAmount() * 10) {
                player.giveExp(-itemStack.getAmount() * 10);
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, itemStack.getAmount()));
                itemStack.setAmount(0);
            } else {
                final int enough = totalExperience / 10;
                player.giveExp(-enough * 10);
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, enough));
                itemStack.setAmount(itemStack.getAmount() - enough);
            }
            getServer().getScheduler().runTask(this, (@NotNull Runnable) player::closeInventory);
        } else {
            player.giveExp(-10);
            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE));
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0F, 1.0F);
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExpBottle(final ExpBottleEvent e) {
        e.setExperience(10);
    }
}
