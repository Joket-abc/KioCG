package com.kiocg.BotExtend.utils;

import com.kiocg.BotExtend.BotExtend;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerLinkUtils {
    // 存储玩家UUID、玩家连接的QQ号
    private static final Map<String, Long> playerLinks = new HashMap<>();
    // 存储等待连接的QQ号、玩家名
    public static final Map<Long, String> waitLinks = new HashMap<>();

    public void loadPlayers() {
        final ConfigurationSection configurationSection = BotExtend.playersFileConfiguration.getConfigurationSection("links");
        Objects.requireNonNull(configurationSection).getValues(false).forEach((uuid, qq) -> playerLinks.put(uuid, Long.valueOf(qq.toString())));
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull String uuidString) {
        return playerLinks.containsKey(uuidString);
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull Long qq) {
        return playerLinks.containsValue(qq);
    }

    public static @Nullable Long getPlayerLinkQQ(final @NotNull String uuidString) {
        return playerLinks.get(uuidString);
    }

    public static @Nullable String getPlayerLinkUUID(final @NotNull Long qq) {
        for (final Map.Entry<String, Long> entry : playerLinks.entrySet()) {
            if (entry.getValue().equals(qq)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static @Nullable String getPlayerLinkName(final @NotNull Long qq) {
        try {
            return Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(getPlayerLinkUUID(qq)))).getName();
        } catch (final @NotNull NullPointerException ignore) {
            return null;
        }
    }

    public static void addPlayerLink(final @NotNull String uuidString, final @NotNull Long qq) {
        playerLinks.put(uuidString, qq);

        BotExtend.playersFileConfiguration.set("links." + uuidString, qq);
        BotExtend.instance.savePlayersFile();
    }
}
