package com.kiocg.java.OtherEvent;

import com.kiocg.java.events.ABEvent;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UserJoinGroup implements Listener {
    @EventHandler
    public void onUserJoinGroup(final ABEvent event) {
        if (!(event.getEvent() instanceof MemberJoinEvent)) {
            return;
        }
        final MemberJoinEvent e = (MemberJoinEvent) event.getEvent();

        final Group group = e.getGroup();
        if (group.getId() == 569696336L) {
            final MessageChain chain = new MessageChainBuilder()
                    .append(new At(e.getMember().getId()))
                    .append("\n欢迎萌新(๑˃̵ᴗ˂̵)و ")
                    .append("\n查看客户端下载地址请输入 .client")
                    .append("\n查看白名单申请方法请输入 .whitelist")
                    .append("\n这里不欢迎熊孩子，请友好相处。 呐。")
                    .build();
            group.sendMessage(chain);
        }
    }
}
