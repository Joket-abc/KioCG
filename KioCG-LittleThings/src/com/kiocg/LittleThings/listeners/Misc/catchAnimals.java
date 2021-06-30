package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class catchAnimals implements Listener {
    // 鸡蛋捕捉动物(大师球)
    @EventHandler(ignoreCancelled = true)
    public void catchAnimals(final @NotNull ProjectileHitEvent e) {
        final Projectile projectile = e.getEntity();
        if (!(projectile instanceof Egg)) {
            return;
        }

        final ProjectileSource projectileSource = projectile.getShooter();
        if (!(projectileSource instanceof Player)) {
            return;
        }

        if (!((Player) projectileSource).hasPermission("kiocg.catchmonsters.use")) {
            return;
        }

        final Entity hitEntity = e.getHitEntity();

        if (!(hitEntity instanceof Animals)) {
            return;
        }

        // 防止捕捉被命名生物
        if (hitEntity.getCustomName() != null) {
            return;
        }

        // 防止捕捉幼年生物
        if (!((Ageable) hitEntity).isAdult()) {
            return;
        }

        // 防止捕捉被驯服生物
        if (hitEntity instanceof Tameable && ((Tameable) hitEntity).isTamed()) {
            return;
        }

        // 防止捕捉没羊毛的羊
        if (hitEntity instanceof Sheep && ((Sheep) hitEntity).isSheared()) {
            return;
        }

        // 防止捕捉有箱子的类马
        if (hitEntity instanceof ChestedHorse && ((ChestedHorse) hitEntity).isCarryingChest()) {
            return;
        }

        // 捕捉概率30%
        if (new Random().nextInt(100) < 70) {
            return;
        }

        final World world = hitEntity.getWorld();
        final Location location = hitEntity.getLocation();

        try {
            world.dropItem(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(hitEntity.getType() + "_SPAWN_EGG"))));
        } catch (final @NotNull NullPointerException ignore) {
            for (final Entity toEntity : world.getNearbyEntities(location, 16.0, 8.0, 16.0)) {
                if (toEntity instanceof Player) {
                    toEntity.sendMessage("§a[§b豆渣子§a] §4捕捉生物失败, 发生内部错误, 请联系管理员!");
                }
            }
            return;
        }

        e.setCancelled(true);

        projectile.remove();
        hitEntity.remove();

        world.createExplosion(location, 0.0F);
        world.playEffect(location, Effect.SMOKE, 0);
    }
}
