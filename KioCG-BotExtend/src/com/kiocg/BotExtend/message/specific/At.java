package com.kiocg.BotExtend.message.specific;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class At {
    public void at(final @NotNull Contact contact, final @NotNull User user, final @NotNull String msg) {
        final String playerName;
        final String messageExpand;

        if (msg.contains(" ")) {
            final int index = msg.indexOf(' ');
            playerName = msg.substring(0, index);
            messageExpand = ": §r" + msg.substring(index + 1);
        } else {
            playerName = msg;
            messageExpand = "";
        }

        if (!Utils.isLegalPlayerName(playerName)) {
            contact.sendMessage("错误的玩家名：" + playerName);
            return;
        }

        final Player player = Bukkit.getPlayerExact(playerName);

        if (player == null) {
            contact.sendMessage("玩家 " + playerName + " 不在线");
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §6群成员 §e" + PlayerLinkUtils.getPlayerLinkName(user.getId()) + " §6提醒你" + messageExpand.trim());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);

        contact.sendMessage("已提醒玩家 " + player.getName());
    }
}
