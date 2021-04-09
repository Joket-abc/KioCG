package com.kiocg.InsaneMonsters;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IMCommand implements @Nullable CommandExecutor {
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§4G§ciant §6| 巨人");
            player.sendMessage("§4Z§combie§4W§carrior §6| 僵尸战士");
            player.sendMessage("§4S§ckeleton§4A§crcher §6| 骷髅弓箭手");
            player.sendMessage("§4D§cark§4K§cnight §6| 黑暗骑士");
            player.sendMessage("§4O§cre§4Z§combie §6| 矿石僵尸");
            player.sendMessage("§4B§clock§4Z§combie §6| 方块僵尸");
            player.sendMessage("§7/insanemonsters <mob> 来生成指定的疯狂怪物.");
            return true;
        }

        if (args.length == 1) {
            final Block block = player.getTargetBlock(64);

            if (block == null) {
                player.sendMessage("§a[§b豆渣子§a] §c所指向的方块距离太远了.");
                return true;
            }

            final Location loc = block.getLocation().toCenterLocation();
            loc.setY(loc.getY() + 1.0);

            switch (args[0].toLowerCase()) {
                case "giant":
                case "g":
                    InsaneMonsters.giant.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成巨人.");
                    break;
                case "zombiewarrior":
                case "zw":
                    InsaneMonsters.zombieWarrior.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成僵尸战士.");
                    break;
                case "skeletonarcher":
                case "sa":
                    InsaneMonsters.skeletonArcher.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成骷髅弓箭手.");
                    break;
                case "darkknight":
                case "dk":
                    InsaneMonsters.darkKnight.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成黑暗骑士.");
                    break;
                case "orezombie":
                case "oz":
                    InsaneMonsters.oreZombie.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成矿石僵尸.");
                    break;
                case "blockzombie":
                case "bz":
                    InsaneMonsters.blockZombie.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成方块僵尸.");
                    break;
                default:
                    player.sendMessage("§a[§b豆渣子§a] §c无效的疯狂怪物种类.");
            }
            return true;
        }

        return false;
    }
}
