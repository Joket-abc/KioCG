package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class DarkKnight {
    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, WitherSkeleton.class);

        livingEntity.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "DarkKnight");
        livingEntity.setCustomName("§cSCP-" + String.format("%03d", new Random().nextInt(6000)));
        livingEntity.setRemoveWhenFarAway(true);

        Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(60.0);
        livingEntity.setHealth(60.0);

        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));

        final ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);

        final EntityEquipment equipment = livingEntity.getEquipment();
        Objects.requireNonNull(equipment).setItemInMainHand(sword);
        equipment.setItemInOffHand(new ItemStack(Material.SHIELD));

        equipment.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        equipment.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        equipment.setItemInMainHandDropChance(0F);
        equipment.setItemInOffHandDropChance(0F);
        equipment.setHelmetDropChance(0F);
        equipment.setChestplateDropChance(0F);
        equipment.setLeggingsDropChance(0F);
        equipment.setBootsDropChance(0F);
    }
}
