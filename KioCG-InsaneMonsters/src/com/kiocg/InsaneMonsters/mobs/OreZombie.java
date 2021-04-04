package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import com.kiocg.InsaneMonsters.Utils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OreZombie {
    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, Zombie.class);

        livingEntity.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "OreZombie");
        livingEntity.setCustomName("§7矿石僵尸");
        livingEntity.setCustomNameVisible(true);
        livingEntity.setRemoveWhenFarAway(true);

        final EntityEquipment equipment = livingEntity.getEquipment();
        Objects.requireNonNull(equipment).setHelmet(new ItemStack(Utils.getOverworldRandomOre()));
        equipment.setHelmetDropChance(1F);
    }
}
