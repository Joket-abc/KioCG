package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.java.events.GroupMessageEvent;
import com.kiocg.java.qqBot;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class GroupAdminMessage implements Listener {
    @EventHandler
    public void onGroupAdminMessage(final GroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final String message = e.getMessage().contentToString();
        final long groupID = e.getGroup().getId();
        final String groupLabel = GAMUtils.getGroupLabel(groupID);
        if (!message.startsWith(groupLabel)) {
            return;
        }

        final long senderID = e.getSender().getId();
        if (!new GAMUtils().isGroupAdmin(groupID, senderID)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                final String cmd = message.substring(groupLabel.length());
                final ConsoleSender sender = new ConsoleSender(e);
                Bukkit.getScheduler().runTask(qqBot.getInstance(), () -> Bukkit.dispatchCommand(sender, cmd));
                final String log = Objects.requireNonNull(BotExtend.getInstance().getConfig().getString("messages.log_command"))
                        .replace("&", "§").replace("%user%", String.valueOf(senderID)).replace("%cmd%", cmd);
                Bukkit.getLogger().info(log);
            }
        }.runTaskAsynchronously(qqBot.getInstance());
    }
}
