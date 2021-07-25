package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.FriendMessageEvent;

public class AsyncMiraiFriendMessageEvent extends AsyncMiraiMessageEvent {
    private final FriendMessageEvent event;

    private boolean cancelledFlag;

    public AsyncMiraiFriendMessageEvent(final FriendMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public FriendMessageEvent getEvent() {
        return event;
    }

    @Override
    public boolean isCancelled() {
        return cancelledFlag;
    }

    @Override
    public void setCancelled(final boolean cancelledFlag) {
        this.cancelledFlag = cancelledFlag;
    }
}
