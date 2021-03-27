package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.FriendMessageEvent;

public class AsyncFriendMessageEvent extends AsyncMessageEvent {
    private final FriendMessageEvent event;

    public AsyncFriendMessageEvent(final FriendMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public FriendMessageEvent getEvent() {
        return event;
    }
}
