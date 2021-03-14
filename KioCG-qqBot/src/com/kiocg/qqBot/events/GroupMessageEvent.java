package com.kiocg.qqBot.events;

public class GroupMessageEvent extends ABEvent {
    private final net.mamoe.mirai.event.events.GroupMessageEvent event;

    public GroupMessageEvent(final net.mamoe.mirai.event.events.GroupMessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public net.mamoe.mirai.event.events.GroupMessageEvent getEvent() {
        return event;
    }
}
