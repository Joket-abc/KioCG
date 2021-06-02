package com.kiocg.LittleThings.listeners.Fun;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class boneMealPlayer implements Listener {
    // 骨粉催熟玩家
    @EventHandler(ignoreCancelled = true)
    public void boneMealPlayer(final @NotNull PlayerInteractEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Entity entityClicked = e.getRightClicked();

        if (!(entityClicked instanceof Player)) {
            return;
        }

        final ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.BONE_MEAL) {
            return;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);
        ((Player) entityClicked).giveExp(1);

        entityClicked.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, entityClicked.getLocation().add(0.0, 1.0, 0.0), 9, 1.0, 1.0, 1.0);
    }
}
