package com.kiocg.BotExtend.GroupMessage.Realize;

import com.kiocg.BotExtend.GroupMessage.GMUtils;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class At {
    public void at(final GroupMessageEvent e, final String msg) {
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
            } catch (final IllegalArgumentException ignore) {
                e.getGroup().sendMessage("非法的UUID：" + playerName);
                return;
            }
            player = Bukkit.getPlayer(uuid);
        } else if (GMUtils.isLegalPlayerName(playerName)) {
            // 输入的可能是玩家名
            player = Bukkit.getPlayer(playerName);
        } else {
            e.getGroup().sendMessage("非法的玩家名：" + playerName);
            return;
        }

        if (player == null) {
            e.getGroup().sendMessage("玩家 " + playerName + " 不在线");
            return;
        }

        player.sendMessage("§7[§b豆渣子§7] §6群成员 §e" + GMUtils.getPlayerLinkAsName(e.getSender().getId()) + " §6提醒你" + messageExpand.trim());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
        e.getGroup().sendMessage("已提醒玩家 " + player.getName());
    }
}
