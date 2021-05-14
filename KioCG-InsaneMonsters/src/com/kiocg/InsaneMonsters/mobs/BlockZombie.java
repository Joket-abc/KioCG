package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockZombie {
    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, Zombie.class);

        livingEntity.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "BlockZombie");
        livingEntity.setCustomName("§7方块僵尸");
        livingEntity.setCustomNameVisible(false);
        livingEntity.setRemoveWhenFarAway(true);

        final EntityEquipment equipment = livingEntity.getEquipment();

        final Material material = location.getBlock().getType();
        if (!material.isAir() && material.isItem()) {
            Objects.requireNonNull(equipment).setHelmet(new ItemStack(material));
        } else {
            location.setY(location.getY() - 1.0);
            final Material materialNew = location.getBlock().getType();

            if (materialNew.isItem() && !materialNew.isAir()) {
                Objects.requireNonNull(equipment).setHelmet(new ItemStack(material));
            } else {
                Objects.requireNonNull(equipment).setHelmet(new ItemStack(Material.STONE));
            }
        }

        equipment.setHelmetDropChance(0.0F);
    }
}
