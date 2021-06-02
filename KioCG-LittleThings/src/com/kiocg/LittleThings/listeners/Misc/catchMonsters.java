package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class catchMonsters implements Listener {
    // 下届之星捕捉怪物
    @EventHandler(ignoreCancelled = true)
    public void catchMonsters(final @NotNull PlayerInteractEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Player player = e.getPlayer();

        if (!player.hasPermission("kiocg.catchmonsters.use")) {
            return;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.NETHER_STAR) {
            return;
        }

        final Entity entity = e.getRightClicked();

        if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER || !(entity instanceof Monster)
            || entity instanceof WitherSkeleton || entity instanceof ElderGuardian || entity instanceof EnderDragon
            || entity instanceof Wither || entity instanceof Giant) {
            return;
        }

        // 防止捕捉幼年生物
        if (entity instanceof Ageable && !((Ageable) entity).isAdult()) {
            return;
        }

        // 防止捕捉被驯服生物
        if (entity instanceof Tameable && ((Tameable) entity).isTamed()) {
            return;
        }

        // 防止捕捉被命名生物
        if (entity.getCustomName() != null) {
            return;
        }

        // 防止捕捉没羊毛的羊
        if (entity instanceof Sheep && ((Sheep) entity).isSheared()) {
            return;
        }

        // 防止捕捉有箱子的类马
        if (entity instanceof ChestedHorse && ((ChestedHorse) entity).isCarryingChest()) {
            return;
        }

        e.setCancelled(true);

        itemStack.setAmount(itemStack.getAmount() - 1);
        entity.remove();

        final World world = entity.getWorld();
        final Location location = entity.getLocation();

        try {
            world.dropItem(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(entity.getType() + "_SPAWN_EGG"))));
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        world.createExplosion(location, 0.0F);
        world.playEffect(location, Effect.SMOKE, 0);
    }
}
