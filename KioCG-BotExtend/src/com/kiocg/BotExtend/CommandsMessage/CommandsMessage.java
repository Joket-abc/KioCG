package com.kiocg.BotExtend.CommandsMessage;

import com.kiocg.qqBot.events.messageEvent.FriendMessageEvent;
import com.kiocg.qqBot.events.messageEvent.GroupMessageEvent;
import com.kiocg.qqBot.events.messageEvent.GroupTempMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CommandsMessage implements @NotNull Listener {
    @EventHandler
    public void onFriendMessage(final @NotNull FriendMessageEvent event) {
        final net.mamoe.mirai.event.events.FriendMessageEvent e = event.getEvent();
        new GroupCommandsPublic().onCommandsPublic(e.getSender(), e.getSender(), e.getMessage().contentToString().trim());
    }

    @EventHandler
    public void onGroupMessage(final @NotNull GroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();
        new GroupCommandsPublic().onCommandsPublic(e.getGroup(), e.getSender(), e.getMessage().contentToString().trim());
    }

    @EventHandler
    public void onGroupTempMessage(final @NotNull GroupTempMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupTempMessageEvent e = event.getEvent();
        new GroupCommandsPublic().onCommandsPublic(e.getGroup(), e.getSender(), e.getMessage().contentToString().trim());
    }
}
