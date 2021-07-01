package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class catchAnimals implements Listener {
    // 鸡蛋捕捉动物(大师球)
    @EventHandler(ignoreCancelled = true)
    public void catchAnimals(final @NotNull EntityDamageByEntityEvent e) {
        final Entity damager = e.getDamager();
        if (!(damager instanceof Egg)) {
            return;
        }

        final Entity entity = e.getEntity();

        if (!(entity instanceof Animals)) {
            return;
        }

        // 防止捕捉被命名生物
        if (entity.getCustomName() != null) {
            return;
        }

        // 防止捕捉幼年生物
        if (!((Ageable) entity).isAdult()) {
            return;
        }

        // 防止捕捉被驯服生物
        if (entity instanceof Tameable && ((Tameable) entity).isTamed()) {
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

        // 捕捉概率30%
        if (new Random().nextInt(100) < 70) {
            return;
        }

        final World world = entity.getWorld();
        final Location location = entity.getLocation();

        try {
            world.dropItem(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(entity.getType() + "_SPAWN_EGG"))));
        } catch (final @NotNull NullPointerException ignore) {
            for (final Entity toEntity : world.getNearbyEntities(location, 16.0, 8.0, 16.0)) {
                if (toEntity instanceof Player) {
                    toEntity.sendMessage("§a[§b豆渣子§a] §4捕捉生物失败, 发生内部错误, 请联系管理员!");
                }
            }
            return;
        }

        e.setCancelled(true);

        damager.remove();
        entity.remove();

        world.createExplosion(location, 0.0F);
        world.playEffect(location, Effect.SMOKE, 0);
    }
}
