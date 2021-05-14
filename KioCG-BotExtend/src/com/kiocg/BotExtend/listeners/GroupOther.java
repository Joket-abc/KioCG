package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.events.AsyncABEvent;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
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

        switch (String.valueOf(Objects.requireNonNull(e.getGroup()).getId())) {
            case "569696336":
                final String message = e.getMessage();
                final String answer = message.substring(message.lastIndexOf('：') + 1);

                // 低等级QQ号需要管理员审核
                final Long userID = e.getFromId();
                if (Mirai.getInstance().queryProfile(e.getBot(), userID).getQLevel() < 16) {
                    if (!Utils.auditQQ.contains(userID)) {
                        if (Utils.isAnswerTrue(answer)) {
                            e.reject(false, "可疑账号，请重新加群并等待管理员审核(可以在回答里留言)");
                            Utils.auditQQ.add(userID);
                        } else {
                            e.reject(false, "回答错误，请检查");
                        }
                    }
                    return;
                }

                if (Utils.isAnswerTrue(answer)) {
                    e.accept();
                } else {
                    e.reject(false, "回答错误，请检查");
                }
                break;
            case "553171328":
                if (PlayerLinkUtils.hasPlayerLink(e.getFromId())) {
                    e.accept();
                } else {
                    e.reject(false, "此群仅限服务器内玩家加入，请连接游戏账号后再来");
                }
                break;
        }
    }

    @EventHandler
    public void onMemberJoin(final @NotNull AsyncABEvent event) {
        if (!(event.getEvent() instanceof final @NotNull MemberJoinEvent e)) {
            return;
        }

        final NormalMember member = e.getMember();
        final long userID = member.getId();

        final Group group = e.getGroup();
        switch (String.valueOf(group.getId())) {
            case "569696336" -> group.sendMessage(new MessageChainBuilder()
                                                          .append(new At(userID)).append(" 欢迎萌新(๑˃̵ᴗ˂̵)و ")
                                                          .append("\n！请先仔细查看群公告！")
                                                          .append("\n有关白名单请输入 .whitelist")
                                                          .append("\n下载客户端请输入 .client")
                                                          .append("\n查看备用IP请输入 .ip")
                                                          .append("\n服务器含假矿+反作弊等保护插件")
                                                          .append("\n这里不欢迎熊孩子，请友好相处。呐。").build());
            case "553171328" -> member.setNameCard(Objects.requireNonNull(PlayerLinkUtils.getPlayerLinkAsName(userID)));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGroupBroadcast(final @NotNull AsyncGroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        if (e.getGroup().getId() != 553171328L || e.getSender().getId() != 1105919949L) {
            return;
        }

        Objects.requireNonNull(e.getBot().getGroup(569696336L)).sendMessage(e.getMessage());
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

            try {
                MessageSource.recall(e.getSource());
            } catch (final @NotNull RuntimeException ignore) {
            }
            event.setCancelled(true);
        }
    }
}
