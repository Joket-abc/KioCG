package com.kiocg.CompressStore;

import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StoryCommand implements @Nullable CommandExecutor {
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
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        final String displayName;

        try {
            displayName = PlainComponentSerializer.plain().serialize(Objects.requireNonNull(itemStack.getItemMeta().displayName()));

            if (!displayName.startsWith("§1§2§6") || !displayName.contains("五次压缩")) {
                player.sendMessage("§a[§b豆渣子§a] §c你的手中没有五次压缩的物品.");
                return true;
            }
        } catch (final @NotNull NullPointerException ignore) {
            player.sendMessage("§a[§b豆渣子§a] §c你的手中没有五次压缩的物品.");
            return true;
        }

        final String permission = "kiocg.story." + itemStack.getType().toString().toLowerCase();

        if (player.hasPermission(permission)) {
            player.sendMessage("§a[§b豆渣子§a] §6你最多只能出售1个" + displayName + ".");
            return true;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + permission);
        Objects.requireNonNull(CompressStore.economy).depositPlayer(player, 1.0);

        player.sendMessage("§a[§b豆渣子§a] §6成功出售1个" + displayName + ", 获得1❣");
        return true;
    }
}
