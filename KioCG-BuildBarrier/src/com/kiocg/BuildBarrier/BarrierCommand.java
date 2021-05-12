package com.kiocg.BuildBarrier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BarrierCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        final Player player = (Player) sender;

        //        if (!Utils.barrierPlayers.contains(player)) {
        //            Utils.barrierPlayers.add(player);
        //            Utils.startBarrier(player);
        //            player.sendMessage("§a[§b豆渣子§a] §6已开启屏障观察模式.");
        //        } else {
        //            Utils.barrierPlayers.remove(player);
        //            Utils.stopBarrier(player);
        //            player.sendMessage("§a[§b豆渣子§a] §6已关闭屏障观察模式.");
        //        }

        Utils.startBarrier(player);
        player.sendMessage("§a[§b豆渣子§a] §6已为你显示周围的屏障.");

        return true;
    }
}
