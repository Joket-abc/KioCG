package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.entity.Slime;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BigSlime {
    public void spawn(final @NotNull Location location) {
        final Slime slime = Objects.requireNonNull(location.getWorld()).spawn(location, org.bukkit.entity.Slime.class);

        slime.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "BigSlime");
        slime.setCustomName("§7巨型史莱姆");
        slime.setRemoveWhenFarAway(true);

        slime.setSize(16);
    }
}
