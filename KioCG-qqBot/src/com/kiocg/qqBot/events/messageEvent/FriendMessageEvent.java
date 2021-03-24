package com.kiocg.qqBot.events.messageEvent;

import org.jetbrains.annotations.NotNull;

public class FriendMessageEvent extends @NotNull MessageEvent {
    private final net.mamoe.mirai.event.events.FriendMessageEvent event;

    public FriendMessageEvent(final net.mamoe.mirai.event.events.FriendMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public net.mamoe.mirai.event.events.FriendMessageEvent getEvent() {
        return event;
    }
}
