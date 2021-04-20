package com.kiocg.BotExtend.listeners;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class InGame implements Listener {
    // 广播被白名单拦截的玩家
    @EventHandler
    public void onProfileWhitelistVerify(final @NotNull ProfileWhitelistVerifyEvent e) {
        if (e.isWhitelisted() || !e.isWhitelistEnabled()) {
            return;
        }

        final PlayerProfile player = e.getPlayerProfile();
        final String playerName = player.getName();

        if (Utils.kickWhitelistPlayer.contains(playerName)) {
            return;
        }
        Utils.kickWhitelistPlayer.add(playerName);

        // 提醒全体玩家
        for (final Player toPlayer : Bukkit.getOnlinePlayers()) {
            toPlayer.sendMessage("§a[§b豆渣子§a] §c不明生物 " + playerName + " 被白名单结界阻挡了.");
        }
        // 控制台记录
        BotExtend.instance.getLogger().info("§c不明生物 " + playerName + " 被白名单结界阻挡了.");

        // 提醒全体群成员
        try {
            KioCGBot.sendGroupMsgAsync(569696336L, "不明生物 " + playerName + " 被白名单结界阻挡了.");
        } catch (final @NotNull Exception ignored) {
        }
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!PlayerLinkUtils.hasPlayerLink(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.instance,
                                                             () -> player.sendMessage("§a[§b豆渣子§a] §6尚未连接QQ号, 请输入 /link 来查看帮助."), 5L);
        }
    }
}
