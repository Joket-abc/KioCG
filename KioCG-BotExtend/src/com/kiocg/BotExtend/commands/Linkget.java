package com.kiocg.BotExtend.commands;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Linkget implements @NotNull CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (args.length != 1) {
            sender.sendMessage("§a[§b豆渣子§a] §c请输入 §4/linkget <mcID/qqID> §c来获取连接的账号.");
            return true;
        }

        // 如果输入的是UUID
        if (args[0].length() == 36) {
            final UUID uuid;

            try {
                uuid = UUID.fromString(args[0]);
            } catch (final @NotNull IllegalArgumentException ignore) {
                sender.sendMessage("§a[§b豆渣子§a] §c非法的UUID: " + args[0]);
                return true;
            }

            final Long qq = PlayerLinkUtils.getPlayerLink(uuid);

            if (qq == null) {
                sender.sendMessage("§a[§b豆渣子§a] §6此UUID未连接QQ号.");
            } else {
                sender.sendMessage("§a[§b豆渣子§a] §2玩家 " + Bukkit.getOfflinePlayer(uuid).getName() + " 连接的QQ号: " + qq);
            }
            return true;
        }

        // 输入的可能是QQ号
        if (StringUtils.isNumeric(args[0])) {
            final Long qq = Long.valueOf(args[0]);
            final String playerName = PlayerLinkUtils.getPlayerLinkAsName(qq);

            if (playerName == null) {
                sender.sendMessage("§a[§b豆渣子§a] §6QQ号 " + qq + " 未连接游戏账号.");
            } else {
                sender.sendMessage("§a[§b豆渣子§a] §2QQ号 " + qq + " 连接的游戏账号: " + playerName);
            }
            // 此处不需要返回, 纯数字也可能是玩家名
        }

        // 输入的可能是玩家名
        if (!Utils.isLegalPlayerName(args[0])) {
            sender.sendMessage("§a[§b豆渣子§a] §c非法的玩家名: " + args[0]);
            return true;
        }

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);

        if (offlinePlayer == null) {
            sender.sendMessage("§a[§b豆渣子§a] §c无法找到玩家 " + args[0] + ", 请尝试使用UUID进行查询.");
            return true;
        }

        final Long qq = PlayerLinkUtils.getPlayerLink(offlinePlayer.getUniqueId());

        if (qq == null) {
            sender.sendMessage("§a[§b豆渣子§a] §6玩家 " + offlinePlayer.getName() + " 未连接QQ号.");
        } else {
            sender.sendMessage("§a[§b豆渣子§a] §2玩家 " + offlinePlayer.getName() + " 连接的QQ号: " + qq);
        }
        return true;
    }
}
