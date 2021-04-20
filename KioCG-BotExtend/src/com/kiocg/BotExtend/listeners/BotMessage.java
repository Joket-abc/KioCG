package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.message.CommandsPublic;
import com.kiocg.BotExtend.message.MessagesRemind;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncFriendMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupTempMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class BotMessage implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onFriendMessage(final @NotNull AsyncFriendMessageEvent event) {
        final net.mamoe.mirai.event.events.FriendMessageEvent e = event.getEvent();

        final String msg = e.getMessage().contentToString().trim();
        if (msg.length() > 100) {
            return;
        }

        final String userCommand = Utils.getUserCommand(msg);
        if (userCommand != null && !userCommand.isEmpty()) {
            new CommandsPublic().onCommandsPublic(e.getSender(), e.getSender(), userCommand);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final String msg = e.getMessage().contentToString().trim();
        if (msg.length() > 100) {
            return;
        }

        final String userCommand = Utils.getUserCommand(msg);
        if (userCommand != null && !userCommand.isEmpty()) {
            new CommandsPublic().onCommandsPublic(e.getGroup(), e.getSender(), userCommand);
        } else {
            new MessagesRemind().onMessages(e.getGroup(), msg.toLowerCase());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGroupTempMessage(final @NotNull AsyncGroupTempMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupTempMessageEvent e = event.getEvent();

        final String msg = e.getMessage().contentToString().trim();
        if (msg.length() > 100) {
            return;
        }

        final String userCommand = Utils.getUserCommand(msg);
        if (userCommand != null && !userCommand.isEmpty()) {
            new CommandsPublic().onCommandsPublic(e.getSender(), e.getSender(), userCommand);
        }
    }
}
