package com.kiocg.AntiAutoFish;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储玩家、鱼上钩时间戳
    public static final Map<Player, Long> playerBiteTime = new HashMap<>();
    // 存储玩家、疑似自动钓鱼次数
    public static final Map<Player, Integer> playerAutoCount = new HashMap<>();
}
