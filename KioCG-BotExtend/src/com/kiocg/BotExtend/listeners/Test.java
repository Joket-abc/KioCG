package com.kiocg.BotExtend.listeners;

import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Test implements Listener {
    @EventHandler
    public void onGroupMessage(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final Group group = e.getGroup();
        if (group.getId() == 547480406L) {
            final MessageChain message = e.getMessage();
            group.sendMessage("contentToString：" + message.contentToString()
                              + "\n\n\n\n"
                              + "serializeToMiraiCode：" + message.serializeToMiraiCode());
        }
    }
}
