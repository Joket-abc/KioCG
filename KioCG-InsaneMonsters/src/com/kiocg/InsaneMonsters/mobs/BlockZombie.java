package com.kiocg.InsaneMonsters.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockZombie {
    private final NamespacedKey namespacedKey;

    public BlockZombie(final NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, Zombie.class);
        livingEntity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "BlockZombie");
        livingEntity.setCustomName("§7方块僵尸");
        livingEntity.setRemoveWhenFarAway(true);

        final EntityEquipment equipment = livingEntity.getEquipment();
        Material material = location.getBlock().getType();
        if (!material.isAir() && material.isItem()) {
            Objects.requireNonNull(equipment).setHelmet(new ItemStack(material));
        } else {
            location.setY(location.getY() - 1.0);
            material = location.getBlock().getType();
            if (material.isItem() && !material.isAir()) {
                Objects.requireNonNull(equipment).setHelmet(new ItemStack(material));
            } else {
                Objects.requireNonNull(equipment).setHelmet(new ItemStack(Material.STONE));
            }
        }
        equipment.setHelmetDropChance(0F);
    }
}
