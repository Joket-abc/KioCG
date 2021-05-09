package com.kiocg.LimitMonsterFarm;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    // 记录怪物出生的位置值
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        final LivingEntity livingEntity = e.getEntity();
        if ((!(livingEntity instanceof Monster) || !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) && !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE)) {
            return;
        }

        livingEntity.getPersistentDataContainer().set(LimitMonsterFarm.namespacedKey, PersistentDataType.LONG, livingEntity.getLocation().toBlockKey());
    }

    // 限制自然刷怪塔
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final Long spawnBlockKey = e.getEntity().getPersistentDataContainer().get(LimitMonsterFarm.namespacedKey, PersistentDataType.LONG);

        if (spawnBlockKey == null) {
            return;
        }

        final EntityType entityType = e.getEntityType();

        if (Utils.limitMonsters.containsKey(spawnBlockKey)) {
            final List<EntityType> monstersList = Utils.limitMonsters.get(spawnBlockKey);

            if (monstersList.contains(entityType)) {
                // 清空怪物掉落
                e.setDroppedExp(0);
                e.getDrops().clear();
            } else {
                monstersList.add(entityType);
            }
        } else {
            Utils.limitMonsters.put(spawnBlockKey, new ArrayList<>() {{
                add(entityType);
            }});
        }
    }
}
