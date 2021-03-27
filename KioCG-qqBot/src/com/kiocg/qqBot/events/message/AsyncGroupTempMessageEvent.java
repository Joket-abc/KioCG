package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.GroupTempMessageEvent;

public class AsyncGroupTempMessageEvent extends AsyncMessageEvent {
    private final GroupTempMessageEvent event;

    public AsyncGroupTempMessageEvent(final GroupTempMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public GroupTempMessageEvent getEvent() {
        return event;
    }
}
