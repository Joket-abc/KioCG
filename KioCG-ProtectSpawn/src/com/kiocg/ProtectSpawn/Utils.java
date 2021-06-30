package com.kiocg.ProtectSpawn;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Utils {
    // 存储主城原点坐标 x、z
    public static final int spawnX = 0;
    public static final int spawnZ = 0;
    // 蛋糕是个谎言
    public static final Set<UUID> eatCake = new HashSet<>();

    public static @NotNull Boolean inSpawn(final @NotNull Location location) {
        //TODO 大版本更新时的世界名修改
        if (!"KioCG_17world".equals(Objects.requireNonNull(location.getWorld()).getName())) {
            return false;
        }

        return Math.abs(spawnX - location.getBlockX()) < 99 && Math.abs(spawnZ - location.getBlockZ()) < 99;
    }
}
