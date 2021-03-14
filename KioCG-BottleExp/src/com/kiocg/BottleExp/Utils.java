package com.kiocg.BottleExp;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Utils {
    // copy PlaceholderAPI/Player-Expansion
    // https://github.com/PlaceholderAPI/Player-Expansion/blob/master/src/main/java/com/extendedclip/papi/expansion/player/PlayerUtil.java#L200
    private int getExperienceAtLevel(final int level) {
        if (level <= 15) {
            return (level << 1) + 7;
        }
        if (level <= 30) {
            return (level * 5) - 38;
        }
        return (level * 9) - 158;
    }

    public int getTotalExperience(final @NotNull Player player) {
        @SuppressWarnings("ImplicitNumericConversion") int experience = Math.round(getExperienceAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();
        while (currentLevel > 0) {
            currentLevel--;
            experience += getExperienceAtLevel(currentLevel);
        }
        if (experience < 0) {
            experience = 0;
        }
        return experience;
    }
    // copy end
}
