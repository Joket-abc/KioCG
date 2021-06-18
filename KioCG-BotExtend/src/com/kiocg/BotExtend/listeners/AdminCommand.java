package com.kiocg.BotExtend.listeners;

import com.google.common.base.Charsets;
import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.ConsoleSender;
import com.kiocg.BotExtend.utils.GroupAdminUtils;
import com.kiocg.BotExtend.utils.HttpsUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import net.mamoe.mirai.contact.Group;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class AdminCommand implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final Group group = e.getGroup();
        final long groupID = group.getId();
        final long senderID = e.getSender().getId();

        if (!GroupAdminUtils.isGroupAdmin(groupID, senderID)) {
            return;
        }

        final String message = e.getMessage().contentToString().trim();
        final String cmd;

        label:
        try {
            for (final String label : Objects.requireNonNull(GroupAdminUtils.getGroupLabels(groupID))) {
                if (message.startsWith(label)) {
                    cmd = message.substring(label.length());
                    break label;
                }
            }
            return;
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        // 拦截白名单提示
        if (cmd.toLowerCase().startsWith("whitelist add ")) {
            group.sendMessage("请使用以下指令给予白名单：\nwhitelist online <正版玩家>\nwhitelist offline <离线玩家>");
            return;
        } else if (cmd.toLowerCase().startsWith("whitelist online ")) {
            final String playerName = cmd.substring(17);

            if (!Utils.isLegalPlayerName(playerName)) {
                group.sendMessage("错误的玩家名：" + playerName);
                return;
            }

            final Boolean onlineAccount = Utils.kickWhitelistPlayer.get(playerName);
            if (onlineAccount == null || !onlineAccount) {
                group.sendMessage("玩家 " + playerName + " 不在正版审核列表中");
                return;
            }

            if (Bukkit.getOfflinePlayer(UUID.fromString("ffffffff-ffff-ffff" + UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8)).toString().substring(18))).hasPlayedBefore()) {
                group.sendMessage("已有同名离线玩家 " + playerName);
                return;
            }

            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + playerName));

            Utils.kickWhitelistPlayer.remove(playerName);
            group.sendMessage("已将正版玩家 " + playerName + " 添加至白名单");
        } else if (cmd.toLowerCase().startsWith("whitelist offline ")) {
            final String playerName = cmd.substring(18);

            if (!Utils.isLegalPlayerName(playerName)) {
                group.sendMessage("错误的玩家名：" + playerName);
                return;
            }

            final Boolean onlineAccount = Utils.kickWhitelistPlayer.get(playerName);
            if (onlineAccount == null || onlineAccount) {
                group.sendMessage("玩家 " + playerName + " 不在离线审核列表中");
                return;
            }

            try {
                if (Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(HttpsUtils.getPlayerUUIDFromApi(playerName)))).hasPlayedBefore()) {
                    group.sendMessage("已有同名正版玩家 " + playerName);
                    return;
                }
            } catch (final @NotNull NullPointerException ignore) {
            }

            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + playerName));

            Utils.kickWhitelistPlayer.remove(playerName);
            group.sendMessage("已将离线玩家 " + playerName + " 添加至白名单");
        } else if (cmd.toLowerCase().startsWith("whitelist remove ")) {
            final String playerName = cmd.substring(17);

            if (!Utils.isLegalPlayerName(playerName)) {
                group.sendMessage("错误的玩家名：" + playerName);
                return;
            }

            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist remove " + playerName));
            group.sendMessage("已尝试将玩家 " + playerName + " 移除出白名单");
        } else {
            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(new ConsoleSender(e), cmd));
        }

        BotExtend.instance.getLogger().info(GroupAdminUtils.logCommand.replace("&", "§")
                                                                      .replace("%user%", String.valueOf(senderID))
                                                                      .replace("%cmd%", cmd));
    }
}
