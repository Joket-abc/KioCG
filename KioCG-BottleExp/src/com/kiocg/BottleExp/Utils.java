package com.kiocg.BottleExp;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Utils {
    // 返回玩家当前的总经验值
    @SuppressWarnings("ImplicitNumericConversion")
    public static int getCurrentTotalExperience(final @NotNull Player player) {
        final int currentLevel = player.getLevel();
        final int currentExperienceAtLevel = Math.round(getCurrentExperienceAtLevel(currentLevel) * player.getExp());

        return getTotalExperienceReachedLevel(currentLevel) + currentExperienceAtLevel;
    }

    // 获取目标等级包含的经验值
    private static int getCurrentExperienceAtLevel(final int level) {
        if (level <= 15) {
            return (level << 1) + 7;
        }
        if (level <= 30) {
            return (level * 5) - 38;
        }
        return (level * 9) - 158;
    }

    // 获取到达目标等级需要的经验值
    @SuppressWarnings("ImplicitNumericConversion")
    private static int getTotalExperienceReachedLevel(final int level) {
        if (level <= 16) {
            return (level * level) + level * 6;
        }
        if (level <= 31) {
            return (int) ((level * level * 2.5) - level * 40.5 + 360.0D);
        }
        return (int) ((level * level * 4.5) - level * 162.5 + 2220.0D);
    }
}
