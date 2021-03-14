package com.kiocg.BotExtend.Commands;

import com.kiocg.BotExtend.GroupMessage.GMUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        final Long qq = GMUtils.getPlayerLink(uuid);
        if (qq != null) {
            player.sendMessage("§a[§b豆渣子§a] §6你已经连接了QQ账号 " + qq + ".");
            return true;
        }
        final GMUtils gmUtils = new GMUtils();
        final Long waitLinkQQ = gmUtils.getWaitLinkQQ(uuid);
        if (waitLinkQQ == null) {
            player.sendMessage("§7[§b豆渣子§7] §6请在群内输入 §e.link " + player.getName() + " §6来连接你的账号.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§7[§b豆渣子§7] §c请输入 §4/link <qq账号> §c来连接此QQ账号.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equals(String.valueOf(waitLinkQQ))) {
                gmUtils.addPlayerLink(uuid, waitLinkQQ);
                gmUtils.removeWaitLinkQQ(uuid);
                player.sendMessage("§a[§b豆渣子§a] §2成功连接了QQ账号 " + waitLinkQQ + ".");
            } else {
                player.sendMessage("§a[§b豆渣子§a] §c你输入的QQ账号有误, 请检查后再试或重新连接.");
            }
            return true;
        }
        return false;
    }
}
