package com.kiocg.PlayOhTheDungeon;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RabbitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            return true;
        }

        if (args.length != 1) {
            return true;
        }

        final String uuid = player.getUniqueId().toString();
        if (args[0].equals(String.valueOf(Utils.playerRabbitConfirm.get(uuid)))) {
            Utils.playerRabbitConfirm.remove(uuid);
            Utils.playerRabbits.put(uuid, Utils.playerRabbits.get(uuid) + 1);

            final int random = new Random().nextInt(100);
            if (random < 70) {
                Utils.joinRabbit(player);
            } else {
                final Location location = player.getLocation();
                location.getWorld().spawn(location, Rabbit.class);
                location.getWorld().spawn(location, Rabbit.class);
                location.getWorld().spawn(location, Rabbit.class);
                player.sendMessage("§a[§b豆渣子§a] §3Ohohoho 好多小兔子~");
            }
        }

        return true;
    }
}
