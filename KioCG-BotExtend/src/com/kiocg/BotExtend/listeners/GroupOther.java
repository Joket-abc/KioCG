package com.kiocg.BotExtend.listeners;

import com.kiocg.qqBot.events.AsyncABEvent;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GroupOther implements Listener {
    @EventHandler
    public void onMemberJoinRequest(final @NotNull AsyncABEvent event) {
        if (!(event.getEvent() instanceof final @NotNull MemberJoinRequestEvent e)) {
            return;
        }

        final String groupID = String.valueOf(Objects.requireNonNull(e.getGroup()).getId());
        if ("569696336".equals(groupID) || "553171328".equals(groupID)) {
            final String message = e.getMessage();
            final String answer = message.substring(message.lastIndexOf('：') + 1);

            if (answer.contains("梯") || answer.toLowerCase().contains("ti") || answer.toLowerCase().contains("ladder")) {
                // 低等级QQ号需要管理员审核
                if (Mirai.getInstance().queryProfile(e.getBot(), e.getFromId()).getQLevel() < 16) {
                    return;
                }

                e.accept();
            } else {
                e.reject(false, "回答错误，请检查后再试~");
            }
        }
    }

    @EventHandler
    public void onMemberJoin(final @NotNull AsyncABEvent event) {
        if (!(event.getEvent() instanceof final @NotNull MemberJoinEvent e)) {
            return;
        }

        final Group group = e.getGroup();
        final String groupID = String.valueOf(group.getId());

        if ("569696336".equals(groupID) || "553171328".equals(groupID)) {
            group.sendMessage(new MessageChainBuilder()
                                      .append(new At(e.getMember().getId())).append(" 欢迎萌新(๑˃̵ᴗ˂̵)و ")
                                      .append("\n！请先仔细查看群公告！")
                                      .append("\n有关白名单请输入 .whitelist")
                                      .append("\n下载客户端请输入 .client")
                                      .append("\n查看备用IP请输入 .ip")
                                      .append("\n服务器含假矿+反作弊等安全插件")
                                      .append("\n这里不欢迎熊孩子，请友好相处。呐。").build());
        }
    }

    // 拦截所有XML消息
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPreventXML(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        final String message = e.getMessage().contentToString();
        if (message.startsWith("<?xml ")) {
            if (message.contains("serviceID=\"33\" action=\"web\" actionData=\"\"")) {
                return;
            }

            event.setCancelled(true);

            try {
                MessageSource.recall(e.getSource());
            } catch (final @NotNull RuntimeException ignore) {
            }
        }
    }
}
