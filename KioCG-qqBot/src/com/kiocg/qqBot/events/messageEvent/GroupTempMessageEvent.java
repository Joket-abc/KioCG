package com.kiocg.qqBot.events.messageEvent;

import org.jetbrains.annotations.NotNull;

public class GroupTempMessageEvent extends @NotNull MessageEvent {
    private final net.mamoe.mirai.event.events.GroupTempMessageEvent event;

    public GroupTempMessageEvent(final net.mamoe.mirai.event.events.GroupTempMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public net.mamoe.mirai.event.events.GroupTempMessageEvent getEvent() {
        return event;
    }
}
