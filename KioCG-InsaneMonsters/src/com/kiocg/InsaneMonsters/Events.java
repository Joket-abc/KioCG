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
import java.util.Random;

public class Events implements @NotNull Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }

        final LivingEntity livingEntity = e.getEntity();
        final Random random = new Random();
        switch (livingEntity.getType()) {
            case ZOMBIE:
                switch (random.nextInt(4)) {
                    case 0:
                        final Location giantLoc = livingEntity.getLocation();
                        if (giantLoc.getBlock().getType().equals(Material.CAVE_AIR) || random.nextInt(1000) < 997) {
                            return;
                        }

                        InsaneMonsters.giant.spawn(giantLoc);
                        break;
                    case 1:
                        if (random.nextInt(100) < 97) {
                            return;
                        }

                        InsaneMonsters.zombieWarrior.spawn(livingEntity.getLocation());
                        break;
                    case 2:
                        final Location oreZombieLoc = livingEntity.getLocation();
                        if (!oreZombieLoc.getBlock().getType().equals(Material.CAVE_AIR) || random.nextInt(100) < 90) {
                            return;
                        }

                        InsaneMonsters.oreZombie.spawn(oreZombieLoc);
                        break;
                    case 3:
                        if (random.nextInt(100) < 90) {
                            return;
                        }

                        InsaneMonsters.blockZombie.spawn(livingEntity.getLocation());
                        break;
                }
                break;
            case SKELETON:
                if (random.nextInt(100) < 97) {
                    return;
                }

                InsaneMonsters.skeletonArcher.spawn(livingEntity.getLocation());
                break;
            case WITHER_SKELETON:
                if (random.nextInt(100) < 97) {
                    return;
                }

                InsaneMonsters.darkKnight.spawn(livingEntity.getLocation());
                break;
            default:
                return;
        }

        e.setCancelled(true);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final String persistentData = e.getEntity().getPersistentDataContainer().get(InsaneMonsters.namespacedKey, PersistentDataType.STRING);
        if (persistentData == null) {
            return;
        }

        final List<ItemStack> drops = e.getDrops();
        final Random random = new Random();
        switch (persistentData) {
            case "Giant":
                final ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                final EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                final Enchantment[] enchantments = Enchantment.values();
                final Enchantment randomEnchantment = enchantments[random.nextInt(enchantments.length)];
                enchantmentStorageMeta.addStoredEnchant(randomEnchantment, randomEnchantment.getMaxLevel(), false);
                itemStack.setItemMeta(enchantmentStorageMeta);
                drops.add(new ItemStack(itemStack));
                break;
            case "ZombieWarrior":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.ROTTEN_FLESH, random.nextInt(3) + 1));
                break;
            case "SkeletonArcher":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.BONE, random.nextInt(3) + 1));
                break;
            case "DarkKnight":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                if (random.nextInt(100) < 10) {
                    drops.add(new ItemStack(Material.NETHERITE_SCRAP));
                }
                break;
            case "OreZombie":
                break;
            case "BlockZombie":
                break;
        }
    }
}
