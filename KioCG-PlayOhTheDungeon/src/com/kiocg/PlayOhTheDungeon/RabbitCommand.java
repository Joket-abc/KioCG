package com.kiocg.PlayOhTheDungeon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RabbitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            return true;
        }

        if (args.length != 1) {
            return true;
        }

        final String uuidString = player.getUniqueId().toString();
        if (args[0].equals(String.valueOf(Utils.playerRabbitConfirm.get(uuidString)))) {
            Utils.playerRabbitConfirm.remove(uuidString);
            Utils.playerRabbits.put(uuidString, Utils.playerRabbits.get(uuidString) + 1);

            Utils.joinRabbit(player);
        }
        return true;
    }
}
