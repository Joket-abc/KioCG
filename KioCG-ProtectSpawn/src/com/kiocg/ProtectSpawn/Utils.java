package com.kiocg.ProtectSpawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Utils {
    // 存储主城原点坐标
    public static final @NotNull Location locSpawn = new Location(Bukkit.getWorld("KioCG_world"), 187.5, 144.0, 209.5);
    // 蛋糕是个谎言
    public static final Set<UUID> eatCake = new HashSet<>();
}
