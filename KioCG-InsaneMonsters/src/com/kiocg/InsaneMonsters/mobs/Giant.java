package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Giant {
    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, org.bukkit.entity.Giant.class);
        livingEntity.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "Giant");
        livingEntity.setRemoveWhenFarAway(true);
    }
}
