package com.kiocg.LittleThings.utility;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储怪物出生的坐标、数据值(怪物类型、次数)
    private static final Map<Location, Map<EntityType, Integer>> spawnLimit = new HashMap<>();
    // 存储半永久禁止刷怪的坐标、次数
    private static final Map<Location, Integer> spawnLimitForever = new HashMap<>();

    // 定时清空生物位置
    public void spawnLimitClear() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LittleThings.instance, spawnLimit::clear, 20L * 60L * 20L, 20L * 60L * 20L);
    }

    // 写入生物限制位置
    public static void addSpawnLimit(final @NotNull Location location, final EntityType entityType) {
        @SuppressWarnings({"IntegerDivisionInFloatingPointContext", "ImplicitNumericConversion"}) final Location newLoc = new Location(location.getWorld(), (location.getBlockX() / 3), location.getBlockY() / 7, location.getBlockZ() / 3);

        if (spawnLimit.containsKey(newLoc)) {
            final Map<EntityType, Integer> value = spawnLimit.get(newLoc);

            if (value.containsKey(entityType)) {
                final int num = value.get(entityType);
                value.put(entityType, num + 1);

                if (num == 3) {
                    spawnLimitForever.put(newLoc, spawnLimitForever.get(newLoc) == null ? 1 : spawnLimitForever.get(newLoc) + 1);
                }
            } else {
                value.put(entityType, 1);
            }
        } else {
            spawnLimit.put(newLoc, new EnumMap<>(EntityType.class) {{
                put(entityType, 1);
            }});
        }
    }

    // 获取生物是否限制
    public static boolean isSpawnLimit(final @NotNull Location location, final EntityType entityType) {
        @SuppressWarnings({"IntegerDivisionInFloatingPointContext", "ImplicitNumericConversion"}) final Location newLoc = new Location(location.getWorld(), location.getBlockX() / 3, location.getBlockY() / 7, location.getBlockZ() / 3);

        if (spawnLimitForever.containsKey(newLoc)) {
            return spawnLimitForever.get(newLoc) > 3;
        }

        if (spawnLimit.containsKey(newLoc)) {
            final Map<EntityType, Integer> value = spawnLimit.get(newLoc);

            if (value.containsKey(entityType)) {
                return value.get(entityType) > 3;
            }
        }

        return false;
    }
}
