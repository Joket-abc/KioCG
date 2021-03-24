package com.kiocg.qqBot.events.messageEvent;

import com.kiocg.qqBot.events.ABEvent;
import org.jetbrains.annotations.NotNull;

public class MessageEvent extends @NotNull ABEvent {
    private final net.mamoe.mirai.event.events.MessageEvent event;

    public MessageEvent(final net.mamoe.mirai.event.events.MessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public net.mamoe.mirai.event.events.MessageEvent getEvent() {
        return event;
    }
}
