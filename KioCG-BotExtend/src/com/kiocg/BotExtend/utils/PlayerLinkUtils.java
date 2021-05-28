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
        for (final Map.Entry<String, Object> entry : Objects.requireNonNull(configurationSection).getValues(false).entrySet()) {
            playerLinks.put(entry.getKey(), Long.valueOf(entry.getValue().toString()));
        }
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull String uuid) {
        return playerLinks.containsKey(uuid);
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull Long qq) {
        return playerLinks.containsValue(qq);
    }

    public static @Nullable Long getPlayerLinkQQ(final @NotNull String uuid) {
        return playerLinks.get(uuid);
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

    public static void addPlayerLink(final @NotNull String uuid, final @NotNull Long qq) {
        playerLinks.put(uuid, qq);

        BotExtend.playersFileConfiguration.set("links." + uuid, qq);
        BotExtend.instance.savePlayersFile();
    }
}
