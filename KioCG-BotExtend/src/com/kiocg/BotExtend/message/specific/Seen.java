package com.kiocg.BotExtend.message.specific;

import com.gmail.nossr50.api.ExperienceAPI;
import com.kiocg.BotExtend.BotExtend;
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
    private final Pattern RGBColor = Pattern.compile("&#.{6}");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void seen(final @NotNull Contact contact, final @NotNull String msg) {
        if (!Utils.isLegalPlayerName(msg)) {
            contact.sendMessage("错误的玩家名：" + msg);
            return;
        }

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);

        if (offlinePlayer == null) {
            contact.sendMessage("无法找到玩家 " + msg + " 的缓存信息");
            return;
        }

        final UUID uuid = offlinePlayer.getUniqueId();

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(RGBColor.matcher(Objects.requireNonNull(ChatColor.stripColor(Utils.getPlayerDisplayName(offlinePlayer)))).replaceAll(""))
                     .append(" (").append(offlinePlayer.isBanned() ? "已封禁" : offlinePlayer.isOnline() ? "在线" : "离线").append(")");

        final Long qq = PlayerLinkUtils.getPlayerLinkQQ(uuid.toString());
        if (qq == null) {
            stringBuilder.append("   QQ：null");
        } else {
            final String qqString = String.valueOf(qq);
            final int length = qqString.length();
            stringBuilder.append("   QQ：").append(qqString, 0, length - 5).append("****").append(qqString, length - 1, length);
        }

        stringBuilder.append("\nUUID：").append(uuid);

        stringBuilder.append("\n初次进入时间：").append(simpleDateFormat.format(offlinePlayer.getFirstPlayed()))
                     .append("\n最后登录时间：").append(simpleDateFormat.format(offlinePlayer.getLastLogin()))
                     .append("\n最后存在时间：").append(simpleDateFormat.format(offlinePlayer.getLastSeen()));

        stringBuilder.append("\n累计在线时间：").append(Utils.ticksToDHMS(offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE)));

        stringBuilder.append("\n死亡次数：").append(offlinePlayer.getStatistic(Statistic.DEATHS)).append("次");
        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
            stringBuilder.append("   元気：").append(ExperienceAPI.getPowerLevelOffline(uuid));
        } else {
            stringBuilder.append("   元気：null");
        }
        try {
            stringBuilder.append("   胖次币：").append(String.format("%.2f", Objects.requireNonNull(BotExtend.economy).getBalance(offlinePlayer) + 0.001));
        } catch (final @NotNull RuntimeException ignore) {
            stringBuilder.append("null");
        }

        contact.sendMessage(stringBuilder.toString());
    }
}
