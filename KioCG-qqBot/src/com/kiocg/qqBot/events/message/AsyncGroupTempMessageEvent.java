package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.GroupTempMessageEvent;

public class AsyncGroupTempMessageEvent extends AsyncMessageEvent {
    private final GroupTempMessageEvent event;

    private boolean cancelledFlag = false;

    public AsyncGroupTempMessageEvent(final GroupTempMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public GroupTempMessageEvent getEvent() {
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
