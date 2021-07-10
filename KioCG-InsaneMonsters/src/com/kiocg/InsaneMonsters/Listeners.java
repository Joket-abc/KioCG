package com.kiocg.InsaneMonsters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        final LivingEntity livingEntity = e.getEntity();

        final Random random = new Random();

        switch (livingEntity.getType()) {
            case ZOMBIE -> {
                switch (random.nextInt(4)) {
                    case 0 -> {
                        final Location giantLoc = livingEntity.getLocation();

                        if (giantLoc.getBlock().getType() == Material.CAVE_AIR || random.nextInt(1000) < 997) {
                            return;
                        }

                        InsaneMonsters.giant.spawn(giantLoc);
                    }
                    case 1 -> {
                        if (random.nextInt(100) < 99) {
                            return;
                        }

                        InsaneMonsters.zombieWarrior.spawn(livingEntity.getLocation());
                    }
                    case 2 -> {
                        final Location oreZombieLoc = livingEntity.getLocation();

                        if (oreZombieLoc.getBlock().getType() != Material.CAVE_AIR || random.nextInt(100) < 90) {
                            return;
                        }

                        InsaneMonsters.oreZombie.spawn(oreZombieLoc);
                    }
                }
            }
            case SKELETON -> {
                if (random.nextInt(100) < 99) {
                    return;
                }

                InsaneMonsters.skeletonArcher.spawn(livingEntity.getLocation());
            }
            case WITHER_SKELETON -> {
                if (random.nextInt(100) < 99) {
                    return;
                }

                InsaneMonsters.darkKnight.spawn(livingEntity.getLocation());
            }
            case SLIME -> {
                if (random.nextInt(100) < 90) {
                    return;
                }

                InsaneMonsters.bigSlime.spawn(livingEntity.getLocation());
            }
            case MAGMA_CUBE -> {
                if (random.nextInt(100) < 90) {
                    return;
                }

                InsaneMonsters.bigMagmaCube.spawn(livingEntity.getLocation());
            }
        }

        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final String persistentData = e.getEntity().getPersistentDataContainer().get(InsaneMonsters.namespacedKey, PersistentDataType.STRING);

        if (persistentData == null) {
            return;
        }

        final List<ItemStack> drops = e.getDrops();

        final Random random = new Random();

        switch (persistentData) {
            case "Giant" -> {
                final ItemStack itemStackG = new ItemStack(Material.ENCHANTED_BOOK);

                final EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemStackG.getItemMeta();
                final Enchantment[] enchantments = Enchantment.values();
                final Enchantment randomEnchantment = enchantments[random.nextInt(enchantments.length)];
                Objects.requireNonNull(enchantmentStorageMeta).addStoredEnchant(randomEnchantment, randomEnchantment.getMaxLevel(), false);

                itemStackG.setItemMeta(enchantmentStorageMeta);

                drops.add(new ItemStack(itemStackG));
            }
            case "ZombieWarrior" -> {
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.ROTTEN_FLESH, random.nextInt(3) + 1));
            }
            case "SkeletonArcher" -> {
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.BONE, random.nextInt(3) + 1));
            }
            case "DarkKnight" -> {
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                if (random.nextInt(100) < 33) {
                    drops.add(new ItemStack(Material.NETHERITE_SCRAP));
                }
            }
        }
    }
}
