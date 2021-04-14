package com.kiocg.LittleThings.utility;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // 存储怪物出生的坐标向量
    public static final List<Vector> spawnVector = new ArrayList<>();

    // 定时清空spawnVector
    public void spawnVectorClear() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LittleThings.instance, spawnVector::clear, 20L * 60L * 20L, 20L * 60L * 20L);
    }
}
