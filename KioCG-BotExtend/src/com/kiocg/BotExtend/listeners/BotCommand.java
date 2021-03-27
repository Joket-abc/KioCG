package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.ConsoleSender;
import com.kiocg.BotExtend.utils.GroupAdminUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BotCommand implements @NotNull Listener {
    @EventHandler
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final long groupID = e.getGroup().getId();
        final long senderID = e.getSender().getId();

        if (!GroupAdminUtils.isGroupAdmin(groupID, senderID)) {
            return;
        }

        final String message = e.getMessage().contentToString().trim();
        String groupLabel = null;

        try {
            for (final String label : Objects.requireNonNull(GroupAdminUtils.getGroupLabels(groupID))) {
                if (message.startsWith(label)) {
                    groupLabel = label;
                    break;
                }
            }

            if (groupLabel == null) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final String cmd = message.substring(groupLabel.length());

        // 拦截白名单提示
        if (cmd.startsWith("whitelist add ")) {
            final String playerName = cmd.substring(14);

            if (Utils.isLegalPlayerName(playerName)) {
                Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));

                e.getGroup().sendMessage("已将玩家 " + playerName + " 添加至白名单");
            } else {
                e.getGroup().sendMessage("非法的玩家名：" + playerName);
                return;
            }
        } else if (cmd.startsWith("whitelist remove ")) {
            final String playerName = cmd.substring(17);

            if (Utils.isLegalPlayerName(playerName)) {
                Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));

                e.getGroup().sendMessage("已将玩家 " + playerName + " 移除出白名单");
            } else {
                e.getGroup().sendMessage("非法的玩家名：" + playerName);
                return;
            }
        } else {
            Bukkit.getScheduler().runTask(BotExtend.instance, () -> Bukkit.dispatchCommand(new ConsoleSender(e), cmd));
        }

        BotExtend.instance.getLogger().info(GroupAdminUtils.logCommand.replace("&", "§")
                                                                      .replace("%user%", String.valueOf(senderID))
                                                                      .replace("%cmd%", cmd));
    }
}
