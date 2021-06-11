package com.kiocg.ProtectSpawn;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Utils {
    //TODO 大版本更新时的坐标修改
    // 存储主城原点坐标 x、z
    public static final int spawnX = 0;
    public static final int spawnZ = 0;
    // 蛋糕是个谎言
    public static final Set<UUID> eatCake = new HashSet<>();

    public static @NotNull Boolean inSpawn(final @NotNull Location location) {
        if (!"KioCG_17world".equals(location.getWorld().getName())) {
            return false;
        }

        return Math.abs(spawnX - location.getBlockX()) < 99 || Math.abs(spawnZ - location.getBlockZ()) < 99;
    }
}
