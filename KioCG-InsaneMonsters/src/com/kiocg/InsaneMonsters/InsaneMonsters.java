package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.DarkKnight;
import com.kiocg.InsaneMonsters.mobs.SkeletonArcher;
import com.kiocg.InsaneMonsters.mobs.ZombieWarrior;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class InsaneMonsters extends JavaPlugin implements Listener {
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "InsaneMonsters");
    private final ZombieWarrior zombieWarrior = new ZombieWarrior(namespacedKey);
    private final SkeletonArcher skeletonArcher = new SkeletonArcher(namespacedKey);
    private final DarkKnight darkKnight = new DarkKnight(namespacedKey);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }

        LivingEntity livingEntity = e.getEntity();
        final Random random = new Random();
        switch (livingEntity.getType()) {
            case ZOMBIE:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = zombieWarrior.spawn(livingEntity.getLocation());
                break;
            case SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = skeletonArcher.spawn(livingEntity.getLocation());
                break;
            case WITHER_SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = darkKnight.spawn(livingEntity.getLocation());
                break;
            default:
                return;
        }
        livingEntity.setCustomName("§cSCP-" + String.format("%03d", random.nextInt(6000)));
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent e) {
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
        }
    }
}
