package com.kiocg.LittleThings.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        try {
            player.teleport(Objects.requireNonNull(player.getBedSpawnLocation()));
        } catch (final @NotNull NullPointerException ignore) {
            player.sendMessage("§a[§b豆渣子§a] §6你的床或已充能的重生锚不存在或已被阻挡.");
        }

        return true;
    }
}
