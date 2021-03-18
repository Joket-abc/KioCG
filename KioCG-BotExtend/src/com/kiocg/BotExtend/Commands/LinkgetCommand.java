package com.kiocg.BotExtend.Commands;

import com.kiocg.BotExtend.GroupMessage.GMUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LinkgetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (args.length == 1) {
            return false;
        }

        // 如果输入的是UUID
        labelUUID:
        if (args[0].length() == 36) {
            final UUID uuid;
            try {
                uuid = UUID.fromString(args[0]);
            } catch (final @NotNull IllegalArgumentException ignore) {
                sender.sendMessage("§7[§b豆渣子§7] §c非法的UUID：" + args[0]);
                break labelUUID;
            }
            final Long qq = GMUtils.getPlayerLink(uuid);
            if (qq == null) {
                sender.sendMessage("§7[§b豆渣子§7] §c此UUID未绑定QQ账号.");
            } else {
                sender.sendMessage("§7[§b豆渣子§7] §2玩家 " + Bukkit.getOfflinePlayer(uuid).getName() + " 绑定的QQ账号: " + qq);
            }
            return true;
        }

        // 如果输入的可能是QQ号
        if (StringUtils.isNumeric(args[0])) {
            final Long qq = Long.valueOf(args[0]);
            final String playerName = GMUtils.getPlayerLinkAsName(qq);
            if (playerName == null) {
                sender.sendMessage("§7[§b豆渣子§7] §cQQ账号 " + qq + " 未绑定游戏账号.");
            } else {
                sender.sendMessage("§7[§b豆渣子§7] §2QQ账号 " + qq + " 绑定的游戏账号: " + playerName);
            }
            // 此处不需要返回, 纯数字也可能是玩家名
        }

        if (!GMUtils.isLegalPlayerName(args[0])) {
            sender.sendMessage("§7[§b豆渣子§7] §c非法的玩家名：" + args[0]);
            return true;
        }

        // 输入的可能是玩家名
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (offlinePlayer == null) {
            sender.sendMessage("§7[§b豆渣子§7] §c无法找到玩家 " + args[0] + ", 请尝试使用UUID进行查询.");
            return true;
        }

        final Long qq = GMUtils.getPlayerLink(offlinePlayer.getUniqueId());
        if (qq == null) {
            sender.sendMessage("§7[§b豆渣子§7] §c玩家 " + offlinePlayer.getName() + " 未绑定QQ账号.");
        } else {
            sender.sendMessage("§7[§b豆渣子§7] §2玩家 " + offlinePlayer.getName() + " 绑定的QQ账号: " + qq);
        }
        return true;
    }
}
