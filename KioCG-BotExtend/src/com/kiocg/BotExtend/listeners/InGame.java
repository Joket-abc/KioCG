package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InGame implements Listener {
    // 广播被白名单拦截的玩家
    @EventHandler
    public void onProfileWhitelistVerify(final @NotNull PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.KICK_WHITELIST) {
            return;
        }

        final Player player = e.getPlayer();
        final String playerName = player.getName();

        final Boolean onlineAccount = Utils.kickWhitelistPlayer.get(playerName);
        if (onlineAccount != null) {
            if (!onlineAccount) {
                e.setKickMessage("\n\n\n\n§7... §c请加群 569696336 申请白名单 §7...\n§6正版账号请使用 play.kiocg.com 登入服务器\n\n\n\n\n\n\n\n\n\n§8只要申请一定会通过的~");
            }
            return;
        }

        final String whitelistMsg;
        if (Objects.requireNonNull(player.getUniqueId()).toString().startsWith("ffffffff-ffff-ffff")) {
            whitelistMsg = "不明生物 " + playerName + " 被离线白名单结界阻挡了.";
            e.setKickMessage("\n\n\n\n§7... §c请加群 569696336 申请白名单 §7...\n§6正版账号请使用 play.kiocg.com 登入服务器\n\n\n\n\n\n\n\n\n\n§8只要申请一定会通过的~");
            Utils.kickWhitelistPlayer.put(playerName, false);
        } else {
            whitelistMsg = "不明生物 " + playerName + " 被正版白名单结界阻挡了.";
            Utils.kickWhitelistPlayer.put(playerName, true);
        }

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
