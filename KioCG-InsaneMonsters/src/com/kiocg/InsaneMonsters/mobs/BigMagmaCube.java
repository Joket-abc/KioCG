package com.kiocg.InsaneMonsters.mobs;

import com.kiocg.InsaneMonsters.InsaneMonsters;
import org.bukkit.Location;
import org.bukkit.entity.MagmaCube;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BigMagmaCube {
    public void spawn(final @NotNull Location location) {
        final MagmaCube magmaCube = Objects.requireNonNull(location.getWorld()).spawn(location, MagmaCube.class);

        magmaCube.getPersistentDataContainer().set(InsaneMonsters.namespacedKey, PersistentDataType.STRING, "BigMagmaCube");
        magmaCube.setRemoveWhenFarAway(true);

        magmaCube.setSize(8);
    }
}
