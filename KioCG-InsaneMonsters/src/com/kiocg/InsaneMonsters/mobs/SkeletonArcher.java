package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class SkeletonArcher {
    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, Skeleton.class);
        livingEntity.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "SkeletonArcher");
        livingEntity.setCustomName("§cSCP-" + String.format("%03d", new Random().nextInt(6000)));
        livingEntity.setRemoveWhenFarAway(true);

        Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(60.0);
        livingEntity.setHealth(60.0);

        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));

        final ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
        bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        final EntityEquipment equipment = livingEntity.getEquipment();
        Objects.requireNonNull(equipment).setItemInMainHand(bow);
        equipment.setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));

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
    }
}
