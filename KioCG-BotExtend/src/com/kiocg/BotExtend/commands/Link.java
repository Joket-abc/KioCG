package com.kiocg.BotExtend.commands;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Link implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();

        final Long qq = PlayerLinkUtils.getPlayerLink(uuid);
        if (qq != null) {
            player.sendMessage("§a[§b豆渣子§a] §6你已经连接了QQ号 " + qq + ".");
            return true;
        }

        final Long waitLinkQQ = PlayerLinkUtils.getWaitLinkQQ(uuid);

        if (waitLinkQQ == null) {
            player.sendMessage("§a[§b豆渣子§a] §6请在群内输入 §e.link " + player.getName() + " §6来连接你的账号.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§a[§b豆渣子§a] §c请输入 §4/link <QQ号> §c来连接此QQ号.");
            return true;
        }

        if (args[0].equals(String.valueOf(waitLinkQQ))) {
            PlayerLinkUtils.addPlayerLink(uuid, waitLinkQQ);
            PlayerLinkUtils.removeWaitLinkQQ(uuid);

            player.sendMessage("§a[§b豆渣子§a] §2成功连接了QQ号 " + waitLinkQQ + ".");
        } else {
            player.sendMessage("§a[§b豆渣子§a] §c你输入的QQ号有误, 请检查后再试或重新连接.");
        }
        return true;
    }
}
