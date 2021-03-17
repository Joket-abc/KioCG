package com.kiocg.BotExtend.OtherEvent;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.GroupMessage.GMUtils;
import com.kiocg.qqBot.events.ABEvent;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OtherEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!GMUtils.hasPlayerLink(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.getInstance(),
                    () -> player.sendMessage("§7[§b豆渣子§7] §6尚未绑定QQ账号, 请输入 /link 来查看帮助."), 5L);
        }
    }

    @EventHandler
    public void onUserJoinGroup(final ABEvent event) {
        if (!(event.getEvent() instanceof MemberJoinEvent)) {
            return;
        }
        final MemberJoinEvent e = (MemberJoinEvent) event.getEvent();

        final Group group = e.getGroup();
        if (group.getId() == 569696336L) {
            group.sendMessage(new MessageChainBuilder()
                    .append(new At(e.getMember().getId())).append(" 欢迎萌新(๑˃̵ᴗ˂̵)و ")
                    .append("\n！请先仔细查看群公告！")
                    .append("\n申请白名单请输入 .whitelist")
                    .append("\n下载客户端请输入 .client")
                    .append("\n查看备用IP请输入 .ip")
                    .append("\n这里不欢迎熊孩子，请友好相处。呐。")
                    .build());
        }
    }
}
