package com.kiocg.BotExtend.listeners;

import com.google.common.base.CharMatcher;
import com.kiocg.BotExtend.message.MessagesCommand;
import com.kiocg.BotExtend.message.MessagesRemind;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncMiraiGroupMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class UserCommand implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onGroupMessage(final @NotNull AsyncMiraiGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final String msg = CharMatcher.whitespace().trimAndCollapseFrom(e.getMessage().contentToString(), ' ');

        if (msg.length() > 100 || msg.length() < 2) {
            return;
        }

        final String userCommand = Utils.getUserCommand(msg);
        if (userCommand != null && !userCommand.isEmpty()) {
            new MessagesCommand().onCommandsPublic(e.getGroup(), userCommand);
        } else {
            new MessagesRemind().onMessages(e.getGroup(), e.getSender(), msg.toLowerCase());
        }
    }
}
