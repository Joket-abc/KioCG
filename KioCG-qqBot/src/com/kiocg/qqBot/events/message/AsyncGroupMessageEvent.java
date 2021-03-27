package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class AsyncGroupMessageEvent extends AsyncMessageEvent {
    private final GroupMessageEvent event;

    private boolean cancelledFlag = false;

    public AsyncGroupMessageEvent(final GroupMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public GroupMessageEvent getEvent() {
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
