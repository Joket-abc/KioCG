package com.kiocg.BotExtend.utils;

import com.kiocg.BotExtend.BotExtend;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {
    // 存储被白名单阻挡的玩家、是否为正版
    public static final Map<String, Boolean> kickWhitelistPlayer = new HashMap<>();

    // 返回是否为合法玩家名
    public static @NotNull Boolean isLegalPlayerName(final @NotNull String string) {
        return Pattern.matches("^[0-9a-zA-Z_]{3,16}$", string);
    }

    // 返回玩家带前后缀的昵称
    public static @Nullable String getPlayerDisplayName(final @NotNull OfflinePlayer offlinePlayer) {
        try {
            return Objects.requireNonNull(BotExtend.chat).getPlayerPrefix(null, offlinePlayer)
                   + offlinePlayer.getName()
                   + BotExtend.chat.getPlayerSuffix(null, offlinePlayer);
        } catch (final @NotNull NullPointerException ignore) {
            return offlinePlayer.getName();
        }
    }

    // 将tick转为天时分秒的格式
    public static @NotNull String ticksToDHMS(final int tick) {
        final int allSeconds = tick / 20;

        final int days = allSeconds / (60 * 60 * 24);
        final int hours = (allSeconds % (60 * 60 * 24)) / (60 * 60);
        final int minutes = (allSeconds % (60 * 60)) / 60;
        final int seconds = allSeconds % 60;

        if (days > 0) {
            return days + "d" + hours + "h" + minutes + "m" + seconds + "s";
        } else if (hours > 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m" + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    // 返回去除指令前缀的用户指令
    public static @Nullable String getUserCommand(final @NotNull String msg) {
        try {
            if ((msg.charAt(0) != '.' && msg.charAt(0) != '。') || msg.charAt(1) == '.' || msg.charAt(1) == '。') {
                return null;
            }
        } catch (final @NotNull IndexOutOfBoundsException ignore) {
            return null;
        }

        return msg.substring(1).trim();
    }
}
