package com.kiocg.qqBot.events.messageEvent;

import org.jetbrains.annotations.NotNull;

public class GroupMessageEvent extends @NotNull MessageEvent {
    private final net.mamoe.mirai.event.events.GroupMessageEvent event;

    public GroupMessageEvent(final net.mamoe.mirai.event.events.GroupMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public net.mamoe.mirai.event.events.GroupMessageEvent getEvent() {
        return event;
    }
}
