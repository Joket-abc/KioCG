package com.kiocg.BotExtend.commands;

import com.kiocg.BotExtend.utils.HttpsUtils;
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

public class Linkget implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (args.length != 1) {
            sender.sendMessage("§a[§b豆渣子§a] §c请输入 §4/linkget <mcID/qqID> §c来获取连接的账号.");
            return true;
        }

        // 输入的可能是QQ号
        if (StringUtils.isNumeric(args[0])) {
            final Long qq = Long.valueOf(args[0]);
            final String playerName = PlayerLinkUtils.getPlayerLinkName(qq);

            if (playerName == null) {
                sender.sendMessage("§a[§b豆渣子§a] §6QQ号 " + qq + " 未连接游戏账号.");
            } else {
                sender.sendMessage("§a[§b豆渣子§a] §2QQ号 " + qq + " 连接的游戏账号: " + playerName);
            }
            // 此处不需要返回, 纯数字也可能是玩家名
        }

        // 输入的可能是玩家名
        if (!Utils.isLegalPlayerName(args[0])) {
            sender.sendMessage("§a[§b豆渣子§a] §c错误的玩家名: " + args[0]);
            return true;
        }

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        final String uuidString;

        if (offlinePlayer == null) {
            final String uuidFromApi = HttpsUtils.getPlayerUUIDFromApi(args[0]);
            if (uuidFromApi == null) {
                sender.sendMessage("正版玩家不存在：" + args[0]);
                return true;
            }
            uuidString = uuidFromApi;

            if (!Bukkit.getOfflinePlayer(UUID.fromString(uuidFromApi)).hasPlayedBefore()) {
                sender.sendMessage("正版玩家 " + args[0] + " 从未出现过");
                return true;
            }
        } else {
            uuidString = offlinePlayer.getUniqueId().toString();
        }

        final Long qq = PlayerLinkUtils.getPlayerLinkQQ(uuidString);

        if (qq == null) {
            sender.sendMessage("§a[§b豆渣子§a] §6玩家 " + args[0] + " 未连接QQ号.");
        } else {
            sender.sendMessage("§a[§b豆渣子§a] §2玩家 " + args[0] + " 连接的QQ号: " + qq);
        }
        return true;
    }
}
