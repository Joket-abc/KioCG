package com.kiocg.qqBot.events.message;

import com.kiocg.qqBot.events.AsyncABEvent;
import net.mamoe.mirai.event.events.MessageEvent;

public class AsyncMessageEvent extends AsyncABEvent {
    private final MessageEvent event;

    public AsyncMessageEvent(final MessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public MessageEvent getEvent() {
        return event;
    }
}
