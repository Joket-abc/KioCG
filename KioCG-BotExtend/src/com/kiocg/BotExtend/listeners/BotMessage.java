package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.message.CommandsPublic;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.message.AsyncFriendMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupTempMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class BotMessage implements @NotNull Listener {
    @EventHandler
    public void onFriendMessage(final @NotNull AsyncFriendMessageEvent event) {
        final net.mamoe.mirai.event.events.FriendMessageEvent e = event.getEvent();

        final String userCommand = Utils.getUserCommand(e.getMessage().contentToString().trim());
        if (userCommand != null) {
            new CommandsPublic().onCommandsPublic(e.getSender(), e.getSender(), userCommand);
        }
    }

    @EventHandler
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final String userCommand = Utils.getUserCommand(e.getMessage().contentToString().trim());
        if (userCommand != null) {
            new CommandsPublic().onCommandsPublic(e.getGroup(), e.getSender(), e.getMessage().contentToString().trim());
        }
    }

    @EventHandler
    public void onGroupTempMessage(final @NotNull AsyncGroupTempMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupTempMessageEvent e = event.getEvent();

        final String userCommand = Utils.getUserCommand(e.getMessage().contentToString().trim());
        if (userCommand != null) {
            new CommandsPublic().onCommandsPublic(e.getSender(), e.getSender(), e.getMessage().contentToString().trim());
        }
    }
}
