package com.kiocg.BotExtend.listeners;

import com.google.common.base.CharMatcher;
import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.BotAdminUtils;
import com.kiocg.BotExtend.utils.CommandSender;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncMiraiGroupMessageEvent;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onGroupMessage(final @NotNull AsyncMiraiGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final Group group = e.getGroup();

        if (group.getBotPermission() == MemberPermission.MEMBER) {
            return;
        }

        if (!(e.getSender() instanceof final @NotNull NormalMember normalMember) || normalMember.getPermission() == MemberPermission.MEMBER) {
            return;
        }

        final String msg = CharMatcher.whitespace().trimAndCollapseFrom(e.getMessage().contentToString(), ' ');

        if (msg.length() > 100 || msg.length() < 2) {
            return;
        }

        final String adminCommand = BotAdminUtils.getAdminCommand(msg);

        if (adminCommand == null || adminCommand.isEmpty()) {
            return;
        }

        final String adminCommandLower = adminCommand.toLowerCase();

        // 拦截白名单提示
        if (adminCommandLower.startsWith("whitelist add ")) {
            final int index = adminCommand.indexOf('@');

            if (index == -1) {
                group.sendMessage("用法：!whitelist add <玩家> <@群成员>");
                return;
            }

            final Long qq = Long.valueOf(adminCommand.substring(index + 1));

            final String playerLinked = PlayerLinkUtils.getQQLinkPlayerName(qq);
            if (playerLinked != null) {
                group.sendMessage("此qq用户已经连接了游戏账号：" + playerLinked);
                return;
            }

            final String playerName = adminCommand.substring(17, index - 1);

            if (!Utils.isLegalPlayerName(playerName)) {
                group.sendMessage("错误的玩家名：" + playerName);
                return;
            }

            if (!Utils.kickWhitelistPlayer.contains(playerName)) {
                group.sendMessage("玩家 " + playerName + " 不在白名单审核列表中");
                return;
            }

            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + playerName));
            //noinspection deprecation
            PlayerLinkUtils.addPlayerLink(Bukkit.getOfflinePlayer(playerName).getUniqueId().toString(), qq);

            Utils.kickWhitelistPlayer.remove(playerName);
            group.sendMessage("已将玩家 " + playerName + " 添加至白名单");
        } else if (adminCommandLower.startsWith("whitelist remove ")) {
            final String playerName = adminCommand.substring(17);

            if (!Utils.isLegalPlayerName(playerName)) {
                group.sendMessage("错误的玩家名：" + playerName);
                return;
            }

            if (!PlayerLinkUtils.hasPlayerLink(playerName)) {
                group.sendMessage("玩家不在白名单列表中");
                return;
            }

            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist remove " + playerName));
            //noinspection deprecation
            PlayerLinkUtils.removePlayerLink(Bukkit.getOfflinePlayer(playerName).getUniqueId().toString());

            group.sendMessage("已将玩家 " + playerName + " 移除出白名单并断开与qq的连接");
        } else {
            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(new CommandSender(e), adminCommand));
        }

        BotExtend.instance.getLogger().info(BotAdminUtils.logCommand.replace("&", "§")
                                                                    .replace("%user%", String.valueOf(normalMember.getId()))
                                                                    .replace("%cmd%", adminCommand));
    }
}
