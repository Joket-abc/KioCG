package com.kiocg.qqBot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ABEvent extends @NotNull Event {
    private final net.mamoe.mirai.event.Event event;
    private static final HandlerList handlers = new HandlerList();

    public ABEvent(final net.mamoe.mirai.event.Event event) {
        super(true);
        this.event = event;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    public net.mamoe.mirai.event.Event getEvent() {
        return event;
    }
}
