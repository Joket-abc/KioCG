package com.kiocg.BotExtend.OtherEvent;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.CommandsMessage.GMUtils;
import com.kiocg.qqBot.bot.KioCGBot;
import com.kiocg.qqBot.events.ABEvent;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OtherEvent implements @NotNull Listener {
    // 存储已发送进群消息的qq, 防止重复发送
    private final List<Long> welcome = new ArrayList<>();
    // 存储低等级用户, 等待管理员二次审核
    private final List<Long> audit = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!GMUtils.hasPlayerLink(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.INSTANCE,
                                                             () -> player.sendMessage("§7[§b豆渣子§7] §6尚未绑定QQ账号, 请输入 /link 来查看帮助."), 5L);
        }
    }

    @EventHandler
    public void onMemberJoin(final @NotNull ABEvent event) {
        if (!(event.getEvent() instanceof MemberJoinEvent)) {
            return;
        }
        final MemberJoinEvent e = (MemberJoinEvent) event.getEvent();

        final Long userID = e.getMember().getId();
        if (welcome.contains(userID)) {
            return;
        } else {
            welcome.add(userID);
        }

        final Group group = e.getGroup();
        if (group.getId() == 569696336L) {
            group.sendMessage(new MessageChainBuilder()
                                      .append(new At(userID)).append(" 欢迎萌新(๑˃̵ᴗ˂̵)و ")
                                      .append("\n！请先仔细查看群公告！")
                                      .append("\n申请白名单请输入 .whitelist")
                                      .append("\n下载客户端请输入 .client")
                                      .append("\n查看备用IP请输入 .ip")
                                      .append("\n这里不欢迎熊孩子，请友好相处。呐。").build());
        }
    }

    @EventHandler
    public void onMemberJoinRequest(final @NotNull ABEvent event) {
        if (!(event.getEvent() instanceof MemberJoinRequestEvent)) {
            return;
        }
        final MemberJoinRequestEvent e = (MemberJoinRequestEvent) event.getEvent();

        if (Objects.requireNonNull(e.getGroup()).getId() == 569696336L) {
            final String message = e.getMessage();
            final String answer = message.substring(message.lastIndexOf('：') + 1).trim();

            // 低等级用户需管理员二次审核
            final Long userID = e.getFromId();
            if (Mirai.getInstance().queryProfile(KioCGBot.getApi().getBot(), userID).getQLevel() < 16) {
                if (audit.contains(userID)) {
                    switch (answer.toLowerCase()) {
                        case ("梯子"):
                        case ("楼梯"):
                        case ("木梯"):
                        case ("ladder"):
                            return;
                        default:
                            e.reject(false, "回答错误，请检查");
                            return;
                    }
                } else {
                    switch (answer.toLowerCase()) {
                        case ("梯子"):
                        case ("楼梯"):
                        case ("木梯"):
                        case ("ladder"):
                            e.reject(false, "高危账号，请重新加群等待管理员审核");
                            audit.add(userID);
                            return;
                        default:
                            e.reject(false, "回答错误，请检查");
                            return;
                    }
                }
            }

            switch (answer.toLowerCase()) {
                case ("梯子"):
                case ("楼梯"):
                case ("木梯"):
                case ("ladder"):
                    e.accept();
                    break;
                default:
                    e.reject(false, "回答错误，请检查");
            }
        }
    }
}
