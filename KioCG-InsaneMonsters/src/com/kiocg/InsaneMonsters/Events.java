package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Events implements @NotNull Listener {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final InsaneMonsters plugin;

    private final NamespacedKey namespacedKey;
    private final @NotNull ZombieWarrior zombieWarrior;
    private final @NotNull SkeletonArcher skeletonArcher;
    private final @NotNull DarkKnight darkKnight;

    private final @NotNull OreZombie oreZombie;
    private final @NotNull BlockZombie blockZombie;

    public Events(final InsaneMonsters insaneMonsters, final NamespacedKey namespacedKey) {
        plugin = insaneMonsters;

        this.namespacedKey = namespacedKey;
        zombieWarrior = new ZombieWarrior(namespacedKey);
        skeletonArcher = new SkeletonArcher(namespacedKey);
        darkKnight = new DarkKnight(namespacedKey);

        oreZombie = new OreZombie(namespacedKey);
        blockZombie = new BlockZombie(namespacedKey);
    }

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
                        // 巨人
                        if (random.nextInt(1000) < 997) {
                            return;
                        }

                        final Location location = livingEntity.getLocation();
                        location.getWorld().spawn(location, Giant.class);
                        break;
                    case 1:
                        if (random.nextInt(100) < 95) {
                            return;
                        }

                        zombieWarrior.spawn(livingEntity.getLocation());
                        break;
                    case 2:
                        final Location loc = livingEntity.getLocation();
                        if (!loc.getBlock().getType().equals(Material.CAVE_AIR) || random.nextInt(100) < 70) {
                            return;
                        }

                        oreZombie.spawn(loc);
                        break;
                    case 3:
                        if (random.nextInt(100) < 70) {
                            return;
                        }

                        blockZombie.spawn(livingEntity.getLocation());
                        break;
                }
                break;
            case SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                skeletonArcher.spawn(livingEntity.getLocation());
                break;
            case WITHER_SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                darkKnight.spawn(livingEntity.getLocation());
                break;
            default:
                return;
        }
        e.setCancelled(true);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final String persistentData = e.getEntity().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        if (persistentData == null) {
            return;
        }

        final List<ItemStack> drops = e.getDrops();
        final Random random = new Random();
        switch (persistentData) {
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
