package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.entity.MagmaCube;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BigMagmaCube {
    public void spawn(final @NotNull Location location) {
        final MagmaCube magmaCube = location.getWorld().spawn(location, MagmaCube.class);

        magmaCube.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "BigMagmaCube");
        magmaCube.setCustomName("§7巨型岩浆怪");
        magmaCube.setRemoveWhenFarAway(true);

        magmaCube.setSize(16);
    }
}
