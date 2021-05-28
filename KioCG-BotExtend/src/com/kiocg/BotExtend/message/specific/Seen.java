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
import java.util.regex.Pattern;

public class Seen {
    private static final Pattern RGBColor = Pattern.compile("&#.{6}");

    public void seen(final @NotNull Contact contact, final @NotNull String msg) {
        if (!Utils.isLegalPlayerName(msg)) {
            contact.sendMessage("错误的玩家名：" + msg);
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);
        final UUID uuid;

        if (offlinePlayer == null) {
            final String uuidString = HttpsUtils.getPlayerUUIDFromApi(msg);
            if (uuidString == null) {
                contact.sendMessage("正版玩家不存在：" + msg);
                return;
            }
            uuid = UUID.fromString(uuidString);
            offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (!offlinePlayer.hasPlayedBefore()) {
                contact.sendMessage("玩家 " + msg + " 从未出现过");
                return;
            }
        } else {
            uuid = offlinePlayer.getUniqueId();
        }

        final String playerName = offlinePlayer.getName();

        if (playerName == null) {
            contact.sendMessage("玩家 " + msg + " 从未出现过");
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(RGBColor.matcher(Objects.requireNonNull(ChatColor.stripColor(Utils.getPlayerDisplayName(offlinePlayer)))).replaceAll(""))
                     .append(" (").append(offlinePlayer.isBanned() ? "已封禁" : offlinePlayer.isOnline() ? "在线" : "离线").append(")")
                     .append("   QQ：").append(PlayerLinkUtils.getPlayerLinkQQ(playerName));

        stringBuilder.append("\nUUID：").append(uuid);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stringBuilder.append("\n初次进入时间：").append(simpleDateFormat.format(offlinePlayer.getFirstPlayed()))
                     .append("\n最后登录时间：").append(simpleDateFormat.format(offlinePlayer.getLastLogin()))
                     .append("\n最后存在时间：").append(simpleDateFormat.format(offlinePlayer.getLastSeen()));

        stringBuilder.append("\n累计在线时间：").append(Utils.ticksToDHMS(offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE)));

        stringBuilder.append("\n死亡次数：").append(offlinePlayer.getStatistic(Statistic.DEATHS)).append("次");
        try {
            stringBuilder.append("   元気：").append(ExperienceAPI.getPowerLevelOffline(uuid));
        } catch (final @NotNull RuntimeException ignore) {
            stringBuilder.append("NULL");
        }
        try {
            stringBuilder.append("   胖次币：").append(Objects.requireNonNull(BotExtend.economy).getBalance(offlinePlayer));
        } catch (final @NotNull RuntimeException ignore) {
            stringBuilder.append("NULL");
        }

        contact.sendMessage(stringBuilder.toString());
    }
}
