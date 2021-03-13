package com.kiocg.java;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Command implements @Nullable CommandExecutor {
    final FoodFlight foodFlight;

    public Command(final @NotNull FoodFlight foodFlight) {
        this.foodFlight = foodFlight;
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command cmd, final @NotNull String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 0) {
            return false;
        }

        final Player player = (Player) sender;
        // 玩家尝试关闭飞行
        if (FoodFlight.flightPlayers.containsKey(player)) {
            try {
                FoodFlight.flightPlayers.get(player).cancel();
            } catch (final NullPointerException ignore) {
            }
            FoodFlight.flightPlayers.remove(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("§a[§b豆渣子§a] §c➷ 关掉关掉一定要关掉 ➷");
            return true;
        }

        // 玩家尝试开启飞行
        switch (player.getWorld().getEnvironment()) {
            case NORMAL:
                if (player.hasPermission("kiocg.foodflight.normal.free")) {
                    FoodFlight.flightPlayers.put(player, null);
                } else if (player.hasPermission("kiocg.foodflight.normal")) {
                    FoodFlight.flightPlayers.put(player, new com.kiocg.java.BukkitTask().flightBukkitTask(foodFlight, player));
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
            case NETHER:
                if (player.hasPermission("kiocg.foodflight.nether.free")) {
                    FoodFlight.flightPlayers.put(player, null);
                } else if (player.hasPermission("kiocg.foodflight.nether")) {
                    FoodFlight.flightPlayers.put(player, new com.kiocg.java.BukkitTask().flightBukkitTask(foodFlight, player));
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
            case THE_END:
                if (player.hasPermission("kiocg.foodflight.end.free")) {
                    FoodFlight.flightPlayers.put(player, null);
                } else if (player.hasPermission("kiocg.foodflight.end")) {
                    FoodFlight.flightPlayers.put(player, new com.kiocg.java.BukkitTask().flightBukkitTask(foodFlight, player));
                } else {
                    player.sendMessage("§a[§b豆渣子§a] §c➷ 不可以在这个世界飞行喔 ➷");
                    return true;
                }
                break;
        }
        player.setAllowFlight(true);
        player.sendMessage("§a[§b豆渣子§a] §a➹ 呼呼, 可以飞了呢 ➹");
        return true;
    }
}
