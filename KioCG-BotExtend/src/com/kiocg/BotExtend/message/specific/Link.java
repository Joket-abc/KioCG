package com.kiocg.BotExtend.message.specific;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Link {
    public void link(final @NotNull Contact contact, final @NotNull User user, final @NotNull String msg) {
        if (!Utils.isLegalPlayerName(msg)) {
            contact.sendMessage("错误的玩家名：" + msg);
            return;
        }

        final Player player = Bukkit.getPlayer(msg);

        if (player == null) {
            contact.sendMessage("玩家 " + msg + " 不在线");
            return;
        }

        final long qq = user.getId();
        PlayerLinkUtils.waitLinks.put(qq, player.getName());
        contact.sendMessage("请再在游戏内输入 /link " + qq + " 来连接此QQ号");
    }
}
