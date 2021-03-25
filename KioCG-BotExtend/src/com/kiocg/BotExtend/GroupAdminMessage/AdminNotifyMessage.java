package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.qqBot.events.messageEvent.GroupMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdminNotifyMessage implements @NotNull Listener {
    @EventHandler
    public void onGroupAdminMessage(final @NotNull GroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        if (e.getGroup().getId() != 553171328L) {
            return;
        }

        Objects.requireNonNull(e.getBot().getGroup(569696336L)).sendMessage(e.getMessage());
    }
}
