package com.kiocg.BuildBarrier.Commands;

import com.kiocg.BuildBarrier.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BarrierCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        Utils.sendBarrierChange(player);
        player.sendMessage("§a[§b豆渣子§a] §6已为你显示周围的屏障30秒.");

        return true;
    }
}
