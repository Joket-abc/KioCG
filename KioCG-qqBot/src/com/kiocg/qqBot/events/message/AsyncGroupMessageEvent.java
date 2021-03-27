package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class AsyncGroupMessageEvent extends AsyncMessageEvent {
    private final GroupMessageEvent event;

    public AsyncGroupMessageEvent(final GroupMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public GroupMessageEvent getEvent() {
        return event;
    }
}
