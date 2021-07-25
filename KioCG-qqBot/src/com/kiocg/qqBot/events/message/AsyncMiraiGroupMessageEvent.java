package com.kiocg.qqBot.events.message;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class AsyncMiraiGroupMessageEvent extends AsyncMiraiMessageEvent {
    private final GroupMessageEvent event;

    private boolean cancelledFlag;

    public AsyncMiraiGroupMessageEvent(final GroupMessageEvent event) {
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
