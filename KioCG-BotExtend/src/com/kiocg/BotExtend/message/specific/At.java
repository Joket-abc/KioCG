package com.kiocg.BotExtend.message.specific;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class At {
    public void at(final @NotNull Contact contact, final @NotNull User user, final @NotNull String msg) {
        final String playerName;
        final String messageExpand;

        if (msg.contains(" ")) {
            playerName = msg.substring(0, msg.indexOf(' '));
            messageExpand = ": §r" + msg.substring(msg.indexOf(' ') + 1);
        } else {
            playerName = msg;
            messageExpand = "";
        }

        final Player player;

        // 如果输入的是UUID
        if (playerName.length() == 36) {
            final UUID uuid;

            try {
                uuid = UUID.fromString(playerName);
            } catch (final @NotNull IllegalArgumentException ignore) {
                contact.sendMessage("非法的UUID：" + playerName);
                return;
            }

            player = Bukkit.getPlayer(uuid);
        } else if (Utils.isLegalPlayerName(playerName)) {
            // 输入的可能是玩家名
            player = Bukkit.getPlayer(playerName);
        } else {
            contact.sendMessage("非法的玩家名：" + playerName);
            return;
        }

        if (player == null) {
            contact.sendMessage("玩家 " + playerName + " 不在线");
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §6群成员 §e" + PlayerLinkUtils.getPlayerLinkAsName(user.getId()) + " §6提醒你" + messageExpand.trim());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);

        contact.sendMessage("已提醒玩家 " + player.getName());
    }
}
