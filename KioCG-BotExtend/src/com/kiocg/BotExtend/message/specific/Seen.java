package com.kiocg.BotExtend.message.specific;

import com.gmail.nossr50.api.ExperienceAPI;
import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.HttpsUtils;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

public class Seen {
    public void seen(final @NotNull Contact contact, final @NotNull String msg) {
        final UUID uuid;
        OfflinePlayer offlinePlayer;

        // 如果输入的是UUID
        if (msg.length() == 36) {
            try {
                uuid = UUID.fromString(msg);
            } catch (final @NotNull IllegalArgumentException ignore) {
                contact.sendMessage("非法的UUID：" + msg);
                return;
            }

            offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (!offlinePlayer.hasPlayedBefore()) {
                contact.sendMessage("玩家UUID " + msg + " 从未出现过");
                return;
            }
        } else if (Utils.isLegalPlayerName(msg)) {
            // 输入的可能是玩家名
            offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);

            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUniqueId();
            } else {
                final String uuidString = HttpsUtils.getPlayerUUIDFromApi(msg);

                if (uuidString == null) {
                    contact.sendMessage("玩家不存在：" + msg);
                    return;
                }

                uuid = UUID.fromString(uuidString);
                offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            }
        } else {
            contact.sendMessage("非法的玩家名：" + msg);
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Objects.requireNonNull(ChatColor.stripColor(Utils.getPlayerDisplayName(offlinePlayer)))
                                    .replaceAll("&#.{6}", ""))
                     .append(" (").append(offlinePlayer.isBanned() ? "已封禁" : offlinePlayer.isOnline() ? "在线" : "离线").append(")")
                     .append("   QQ：").append(PlayerLinkUtils.getPlayerLink(uuid));

        stringBuilder.append("\nUUID：").append(uuid);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stringBuilder.append("\n初次进入时间：").append(simpleDateFormat.format(offlinePlayer.getFirstPlayed()))
                     .append("\n最后登录时间：").append(simpleDateFormat.format(offlinePlayer.getLastLogin()))
                     .append("\n最后存在时间：").append(simpleDateFormat.format(offlinePlayer.getLastSeen()));

        stringBuilder.append("\n累计在线时间：").append(Utils.ticksToDHMS(offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE)));

        stringBuilder.append("\n死亡次数：").append(offlinePlayer.getStatistic(Statistic.DEATHS)).append("次");

        try {
            stringBuilder.append("   元気：").append(ExperienceAPI.getPowerLevelOffline(uuid));
        } catch (final @NotNull Exception ignore) {
            stringBuilder.append("NULL");
        }

        try {
            stringBuilder.append("   胖次币：").append(Objects.requireNonNull(BotExtend.economy).getBalance(offlinePlayer));
        } catch (final @NotNull Exception ignore) {
            stringBuilder.append("NULL");
        }

        contact.sendMessage(stringBuilder.toString());
    }
}
