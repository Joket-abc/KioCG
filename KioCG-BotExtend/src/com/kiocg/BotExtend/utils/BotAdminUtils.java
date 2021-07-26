package com.kiocg.BotExtend.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BotAdminUtils {
    public static @NotNull String logCommand = "";

    // 加载配置文件
    public void loadConfig(final @NotNull FileConfiguration config) {
        logCommand = Objects.requireNonNull(config.getString("logCommand", "&cQQ用户 %user% 执行了指令: %cmd%"));
    }

    // 返回去除指令前缀的管理指令
    public static @Nullable String getAdminCommand(final @NotNull String msg) {
        for (final char label : new char[]{'!', '！'}) {
            if (msg.charAt(0) == label) {
                if (msg.charAt(1) != label) {
                    return msg.substring(1);
                }
                return null;
            }
        }

        return null;
    }
}
