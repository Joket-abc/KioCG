package com.kiocg.qqBot.events;

import org.jetbrains.annotations.NotNull;

public class GroupMessageEvent extends @NotNull ABEvent {
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
