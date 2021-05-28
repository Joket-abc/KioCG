package com.kiocg.BotExtend.message.specific;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Link {
    public void link(final @NotNull Contact contact, final @NotNull User user, final @NotNull String msg) {
        if (!Utils.isLegalPlayerName(msg)) {
            contact.sendMessage("错误的玩家名：" + msg);
            return;
        }

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(msg);

        if (offlinePlayer == null) {
            contact.sendMessage("无法找到玩家 " + msg + "，请上线后再试");
            return;
        }

        final String playerName = offlinePlayer.getName();

        if (playerName == null) {
            contact.sendMessage("无法找到玩家 " + msg + "，请上线后再试");
            return;
        }

        final long qq = user.getId();

        PlayerLinkUtils.waitLinks.put(qq, playerName);

        if (!offlinePlayer.isOnline()) {
            contact.sendMessage("请上线后再在游戏内输入 /link " + qq + " 来连接此QQ号");
        } else {
            contact.sendMessage("请再在游戏内输入 /link " + qq + " 来连接此QQ号");
        }
    }
}
