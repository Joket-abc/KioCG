package com.kiocg.BotExtend.GroupMessage.Realize;

import com.kiocg.BotExtend.GroupMessage.GMUtils;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Link {
    public void link(final GroupMessageEvent e, final String msg) {
        final UUID uuid;
        final OfflinePlayer offlinePlayer;
        // 如果输入的是UUID
        if (msg.length() == 36) {
            try {
                uuid = UUID.fromString(msg);
            } catch (final IllegalArgumentException ignore) {
                e.getGroup().sendMessage("非法的UUID：" + msg);
                return;
            }
            offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (!offlinePlayer.hasPlayedBefore()) {
                e.getGroup().sendMessage("玩家UUID " + msg + " 从未出现过");
                return;
            }
        } else if (GMUtils.isLegalPlayerName(msg)) {
            // 输入的可能是玩家名
            offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);
            if (offlinePlayer == null) {
                e.getGroup().sendMessage("无法找到玩家 " + msg + ", 请尝试使用UUID进行查询.");
                return;
            }
            uuid = offlinePlayer.getUniqueId();
        } else {
            e.getGroup().sendMessage("非法的玩家名：" + msg);
            return;
        }

        new GMUtils().addPlayerLink(uuid, e.getSender().getId());
        if (!offlinePlayer.isOnline()) {
            e.getGroup().sendMessage("请上线后再在游戏内输入 /link " + e.getSender().getId() + " 来连接此QQ账号");
        } else {
            e.getGroup().sendMessage("请在游戏内输入 /link " + e.getSender().getId() + " 来连接此QQ账号");
        }
    }
}
