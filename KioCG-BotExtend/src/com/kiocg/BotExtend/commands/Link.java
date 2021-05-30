package com.kiocg.BotExtend.commands;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Link implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        final String uuidString = player.getUniqueId().toString();

        final Long qq = PlayerLinkUtils.getPlayerLinkQQ(uuidString);
        if (qq != null) {
            player.sendMessage("§a[§b豆渣子§a] §6你已经连接了QQ号 " + qq + ".");
            return true;
        }

        final String playerName = player.getName();

        if (!PlayerLinkUtils.waitLinks.containsValue(playerName)) {
            player.sendMessage("§a[§b豆渣子§a] §6请在群内输入 §e.link " + playerName + " §6来连接你的账号.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§a[§b豆渣子§a] §c请输入 §4/link <QQ号> §c来连接此QQ号.");
            return true;
        }

        final Long waitLinkQQ;
        try {
            waitLinkQQ = Long.parseLong(args[0]);
        } catch (final @NotNull NumberFormatException ignore) {
            player.sendMessage("§a[§b豆渣子§a] §c你输入的QQ号有误, 请检查后再试.");
            return true;
        }

        if (playerName.equals(PlayerLinkUtils.waitLinks.get(waitLinkQQ))) {
            PlayerLinkUtils.addPlayerLink(uuidString, waitLinkQQ);
            PlayerLinkUtils.waitLinks.remove(waitLinkQQ);

            player.sendMessage("§a[§b豆渣子§a] §2成功连接了QQ号 " + waitLinkQQ + ".");
        } else {
            player.sendMessage("§a[§b豆渣子§a] §c你输入的QQ号有误, 请检查后再试.");
        }
        return true;
    }
}
