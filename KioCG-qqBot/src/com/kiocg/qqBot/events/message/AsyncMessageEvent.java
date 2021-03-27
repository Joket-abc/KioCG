package com.kiocg.qqBot.events.message;

import com.kiocg.qqBot.events.AsyncABEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.bukkit.event.Cancellable;

public class AsyncMessageEvent extends AsyncABEvent implements Cancellable {
    private final MessageEvent event;

    private boolean cancelledFlag = false;

    public AsyncMessageEvent(final MessageEvent event) {
        super(event);
        this.event = event;
    }

    @Override
    public MessageEvent getEvent() {
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
