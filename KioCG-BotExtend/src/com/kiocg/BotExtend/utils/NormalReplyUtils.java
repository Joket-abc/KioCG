package com.kiocg.BotExtend.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NormalReplyUtils {
    // 存储关键词、回复消息
    private static final Map<String, String> reply = new HashMap<>();

    @SuppressWarnings("CodeBlock2Expr")
    public void loadConfig(final @NotNull FileConfiguration config) {
        config.getKeys(false).forEach(key -> {
            config.getStringList(key + ".keyword").forEach(keyword -> {
                reply.put(keyword.toLowerCase(), config.getString(key + ".message"));
            });
        });
    }

    public static @Nullable String getNormalReply(final String command) {
        return reply.get(command);
    }
}
