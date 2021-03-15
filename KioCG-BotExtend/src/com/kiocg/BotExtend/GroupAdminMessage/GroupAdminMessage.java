package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.qqBot.events.GroupMessageEvent;
import com.kiocg.qqBot.qqBot;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class GroupAdminMessage implements Listener {
    @EventHandler
    public void onGroupAdminMessage(final GroupMessageEvent event) {
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
        } catch (final NullPointerException ignore) {
            return;
        }
        if (groupLabel == null) {
            return;
        }

        final String cmd = message.substring(groupLabel.length());
        new BukkitRunnable() {
            @Override
            public void run() {
                final ConsoleSender sender = new ConsoleSender(e);
                Bukkit.getScheduler().runTask(qqBot.getInstance(), () -> Bukkit.dispatchCommand(sender, cmd));
                Bukkit.getLogger().info(new GAMUtils().getLogMessage().replace("&", "§")
                        .replace("%user%", String.valueOf(senderID)).replace("%cmd%", cmd));
            }
        }.runTaskAsynchronously(qqBot.getInstance());
    }
}
