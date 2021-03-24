package com.kiocg.BotExtend.CommandsMessage.Realize;

import com.kiocg.BotExtend.CommandsMessage.GMUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Link {
    public void link(final @NotNull Contact contact, final @NotNull User user, final @NotNull String msg) {
        final UUID uuid;
        final OfflinePlayer offlinePlayer;
        // 如果输入的是UUID
        if (msg.length() == 36) {
            try {
                uuid = UUID.fromString(msg);
            } catch (final @NotNull IllegalArgumentException ignore) {
                contact.sendMessage("非法的UUID：" + msg);
                return;
            }
            offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (!offlinePlayer.hasPlayedBefore()) {
                contact.sendMessage("玩家UUID " + msg + " 从未出现过");
                return;
            }
        } else if (GMUtils.isLegalPlayerName(msg)) {
            // 输入的可能是玩家名
            offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);
            if (offlinePlayer == null) {
                contact.sendMessage("无法找到玩家 " + msg + "，请尝试使用UUID进行查询.");
                return;
            }
            uuid = offlinePlayer.getUniqueId();
        } else {
            contact.sendMessage("非法的玩家名：" + msg);
            return;
        }

        GMUtils.addPlayerLink(uuid, user.getId());
        if (!offlinePlayer.isOnline()) {
            contact.sendMessage("请上线后再在游戏内输入 /link " + user.getId() + " 来连接此QQ账号");
        } else {
            contact.sendMessage("请在游戏内输入 /link " + user.getId() + " 来连接此QQ账号");
        }
    }
}
