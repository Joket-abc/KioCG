package com.kiocg.BotExtend.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.kiocg.BotExtend.BotExtend;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class PlayerLinkUtils {
    // 存储玩家UUID、玩家连接的QQ号
    private static final BiMap<String, Long> playerLinks = HashBiMap.create();

    public void loadPlayers(final @NotNull Configuration config) {
        config.getValues(false).forEach((uuid, qq) -> playerLinks.put(uuid, Long.valueOf(qq.toString())));
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull String uuidString) {
        return playerLinks.containsKey(uuidString);
    }

    public static @NotNull Boolean hasQQLink(final @NotNull Long qq) {
        return playerLinks.containsValue(qq);
    }

    public static @Nullable Long getPlayerLink(final @NotNull String uuidString) {
        return playerLinks.get(uuidString);
    }

    public static @Nullable String getQQLink(final @NotNull Long qq) {
        return playerLinks.inverse().get(qq);
    }

    public static @Nullable String getQQLinkPlayerName(final @NotNull Long qq) {
        try {
            return Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(getQQLink(qq)))).getName();
        } catch (final @NotNull NullPointerException ignore) {
            return null;
        }
    }

    public static void addPlayerLink(final @NotNull String uuidString, final @NotNull Long qq) {
        playerLinks.put(uuidString, qq);

        BotExtend.playersFileConfiguration.set(uuidString, qq);
        BotExtend.instance.savePlayersFile();
    }

    public static void removePlayerLink(final @NotNull String uuidString) {
        playerLinks.remove(uuidString);

        BotExtend.playersFileConfiguration.set(uuidString, null);
        BotExtend.instance.savePlayersFile();
    }
}
