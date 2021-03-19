package com.kiocg.InsaneMonsters.mobs;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Giant {
    private final NamespacedKey namespacedKey;

    public Giant(final NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public void spawn(final @NotNull Location location) {
        final LivingEntity livingEntity = location.getWorld().spawn(location, org.bukkit.entity.Giant.class);
        livingEntity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "Giant");
        livingEntity.setRemoveWhenFarAway(true);
    }
}
