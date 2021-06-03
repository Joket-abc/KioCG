package com.kiocg.BotExtend.listeners;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
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

        final String playerName = e.getPlayerProfile().getName();

        if (Utils.kickWhitelistPlayer.contains(playerName)) {
            return;
        }
        Utils.kickWhitelistPlayer.add(playerName);

        final String whitelistMsg = "不明生物 " + playerName + " 被白名单结界阻挡了.";

        // 提醒全体玩家
        Bukkit.getOnlinePlayers().forEach(toPlayer -> toPlayer.sendMessage("§a[§b豆渣子§a] §c" + whitelistMsg));
        // 控制台记录
        BotExtend.instance.getLogger().info("§c" + whitelistMsg);

        // 提醒全体群成员
        try {
            KioCGBot.sendGroupMsgAsync(569696336L, whitelistMsg);
        } catch (final @NotNull RuntimeException ignored) {
        }
    }

    // 提醒玩家连接QQ号
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!PlayerLinkUtils.hasPlayerLink(player.getUniqueId().toString())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.instance,
                                                             () -> player.sendMessage("§a[§b豆渣子§a] §6未连接QQ号, 请在群里输入 §e.link " + player.getName() + " §6来连接."), 5L);
        }
    }
}
