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
    private static final Map<UUID, Long> playerLinks = new HashMap<>();
    // 存储玩家UUID、等待连接的QQ号
    private static final Map<UUID, Long> waitLinks = new HashMap<>();

    public void loadPlayers() {
        final ConfigurationSection configurationSection = BotExtend.playersFileConfiguration.getConfigurationSection("links");
        for (final Map.Entry<String, Object> entry : Objects.requireNonNull(configurationSection).getValues(false).entrySet()) {
            playerLinks.put(UUID.fromString(entry.getKey()), Long.valueOf(entry.getValue().toString()));
        }
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull UUID uuid) {
        return playerLinks.containsKey(uuid);
    }

    public static @NotNull Boolean hasPlayerLink(final @NotNull Long qq) {
        return playerLinks.containsValue(qq);
    }

    public static @Nullable Long getPlayerLink(final @NotNull UUID uuid) {
        return playerLinks.get(uuid);
    }

    public static @Nullable UUID getPlayerLink(final @NotNull Long qq) {
        for (final Map.Entry<UUID, Long> entry : playerLinks.entrySet()) {
            if (entry.getValue().equals(qq)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static @Nullable String getPlayerLinkAsName(final @NotNull Long qq) {
        try {
            return Bukkit.getOfflinePlayer(Objects.requireNonNull(getPlayerLink(qq))).getName();
        } catch (final @NotNull NullPointerException ignore) {
            return null;
        }
    }

    public static void addPlayerLink(final @NotNull UUID uuid, final @NotNull Long qq) {
        playerLinks.put(uuid, qq);

        BotExtend.playersFileConfiguration.set("links." + uuid, qq);
        BotExtend.instance.savePlayersFile();
    }

    public static @Nullable Long getWaitLinkQQ(final @NotNull UUID uuid) {
        return waitLinks.get(uuid);
    }

    public static void addWaitLinkQQ(final @NotNull UUID uuid, final @NotNull Long qq) {
        waitLinks.put(uuid, qq);
    }

    public static void removeWaitLinkQQ(final @NotNull UUID uuid) {
        waitLinks.remove(uuid);
    }
}
