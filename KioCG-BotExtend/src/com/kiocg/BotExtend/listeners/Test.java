package com.kiocg.BotExtend.listeners;

import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Test implements Listener {
    @EventHandler
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        if (e.getGroup().getId() != 547480406L) {
            return;
        }

        e.getGroup().sendMessage("contentToString：" + e.getMessage().contentToString()
                                 + "\n\n\n\n"
                                 + "serializeToMiraiCode：" + e.getMessage().serializeToMiraiCode());
    }
}
