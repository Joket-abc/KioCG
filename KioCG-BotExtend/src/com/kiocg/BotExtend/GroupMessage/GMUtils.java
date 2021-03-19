package com.kiocg.BotExtend.GroupMessage;

import com.kiocg.BotExtend.BotExtend;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class GMUtils {
    // 存储玩家UUID、玩家绑定的QQ
    private static final Map<UUID, Long> playerLinks = new HashMap<>();
    // 存储等待连接QQ的玩家UUID
    private static final Map<UUID, Long> waitLink = new HashMap<>();

    public void loadPlayers() {
        final ConfigurationSection configurationSection = BotExtend.getInstance()
                                                                   .getPlayersFileConfiguration().getConfigurationSection("links");
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

    public void addPlayerLink(final @NotNull UUID uuid, final @NotNull Long qq) {
        playerLinks.put(uuid, qq);
        BotExtend.getInstance().getPlayersFileConfiguration().set("links." + uuid, qq);
        BotExtend.getInstance().savePlayersFile();
    }

    public @Nullable Long getWaitLinkQQ(final @NotNull UUID uuid) {
        return waitLink.get(uuid);
    }

    public void addWaitLinkQQ(final @NotNull UUID uuid, final @NotNull Long qq) {
        waitLink.put(uuid, qq);
    }

    public void removeWaitLinkQQ(final @NotNull UUID uuid) {
        waitLink.remove(uuid);
    }

    // 返回是否为合法玩家名
    public static @NotNull Boolean isLegalPlayerName(final @NotNull String string) {
        return Pattern.compile("^[0-9a-zA-Z_]{3,16}$").matcher(string).matches();
    }

    // 返回玩家带前后缀的名称
    public @Nullable String getPlayerDisplayName(final @NotNull OfflinePlayer offlinePlayer) {
        try {
            final Chat chat = BotExtend.getInstance().getChat();
            return Objects.requireNonNull(chat).getPlayerPrefix(null, offlinePlayer)
                   + offlinePlayer.getName()
                   + chat.getPlayerSuffix(null, offlinePlayer);
        } catch (final @NotNull NullPointerException ignore) {
            return offlinePlayer.getName();
        }
    }

    // 将tick转为天时分秒的格式
    public @NotNull String ticksToDHMS(final int tick) {
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
}
