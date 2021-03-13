package com.kiocg.java.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ZombieWarrior {
    private final NamespacedKey namespacedKey;

    public ZombieWarrior(final NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public LivingEntity spawn(final Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, Zombie.class);
        livingEntity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "ZombieWarrior");
        livingEntity.setRemoveWhenFarAway(true);

        Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(60.0);
        livingEntity.setHealth(60.0);

        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        final EntityEquipment equipment = livingEntity.getEquipment();
        Objects.requireNonNull(equipment).setItemInMainHand(sword);

        equipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        equipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        equipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        equipment.setItemInMainHandDropChance(0F);
        equipment.setItemInOffHandDropChance(0F);
        equipment.setHelmetDropChance(0F);
        equipment.setChestplateDropChance(0F);
        equipment.setLeggingsDropChance(0F);
        equipment.setBootsDropChance(0F);

        return livingEntity;
    }
}
