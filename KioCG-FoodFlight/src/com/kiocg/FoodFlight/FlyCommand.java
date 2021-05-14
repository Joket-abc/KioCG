package com.kiocg.FoodFlight;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        // 玩家尝试关闭飞行
        if (Utils.inFlightList(player)) {
            Utils.removeFlightList(player);

            player.sendMessage("§a[§b豆渣子§a] §6➷ 关掉关掉一定要关掉 ➷");
            return true;
        }

        // 玩家尝试开启飞行
        switch (player.getWorld().getEnvironment()) {
            case NORMAL:
                if (player.hasPermission("kiocg.foodflight.normal.free")) {
                    Utils.addFlightList(player, false);
                } else if (player.hasPermission("kiocg.foodflight.normal")) {
                    if (player.getLocation().getBlockY() < 0) {
                        player.sendMessage("§a[§b豆渣子§a] §c➷ 这里的空气太稀薄了 ➷");
                        return true;
                    }

                    Utils.addFlightList(player, true);
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
            case NETHER:
                if (player.hasPermission("kiocg.foodflight.nether.free")) {
                    Utils.addFlightList(player, false);
                } else if (player.hasPermission("kiocg.foodflight.nether")) {
                    if (player.getLocation().getBlockY() < 0) {
                        player.sendMessage("§a[§b豆渣子§a] §c➷ 这里的空气太稀薄了 ➷");
                        return true;
                    }

                    Utils.addFlightList(player, true);
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
            case THE_END:
                if (player.hasPermission("kiocg.foodflight.end.free")) {
                    Utils.addFlightList(player, false);
                } else if (player.hasPermission("kiocg.foodflight.end")) {
                    if (player.getLocation().getBlockY() < 0) {
                        player.sendMessage("§a[§b豆渣子§a] §c➷ 这里的空气太稀薄了 ➷");
                        return true;
                    }

                    Utils.addFlightList(player, true);
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
        }

        player.setAllowFlight(true);

        player.sendMessage("§a[§b豆渣子§a] §2➹ 呼呼, 可以飞了呢 ➹");
        return true;
    }
}
