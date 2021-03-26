package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.qqBot.events.message.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdminCommandsMessage implements @NotNull Listener {
    @EventHandler
    public void onGroupAdminMessage(final @NotNull GroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final long groupID = e.getGroup().getId();
        final long senderID = e.getSender().getId();
        if (!GAMUtils.isGroupAdmin(groupID, senderID)) {
            return;
        }

        final String message = e.getMessage().contentToString().trim();
        String groupLabel = null;
        try {
            for (final String label : Objects.requireNonNull(GAMUtils.getGroupLabels(groupID))) {
                if (message.startsWith(label)) {
                    groupLabel = label;
                    break;
                }
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }
        if (groupLabel == null) {
            return;
        }

        final String cmd = message.substring(groupLabel.length());
        // 拦截白名单提示
        if (cmd.startsWith("whitelist add ")) {
            try {
                Bukkit.getScheduler().runTask(BotExtend.INSTANCE, () -> e.getGroup().sendMessage("已添加 " + cmd.substring(14) + " 至白名单"));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } catch (final @NotNull IndexOutOfBoundsException ignore) {
                Bukkit.getScheduler().runTask(BotExtend.INSTANCE, () -> e.getGroup().sendMessage("未输入玩家名"));
            }
        } else {
            Bukkit.dispatchCommand(new ConsoleSender(e), cmd);
        }
        BotExtend.INSTANCE.getLogger().info(GAMUtils.logCommand.replace("&", "§")
                                                               .replace("%user%", String.valueOf(senderID))
                                                               .replace("%cmd%", cmd));
    }
}
