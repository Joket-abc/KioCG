package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.*;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IMCommand implements @Nullable CommandExecutor {
    private final NamespacedKey namespacedKey;

    public IMCommand(final NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    @SuppressWarnings("RedundantSuppression")
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        final Player player = (Player) sender;
        if (args.length == 0) {
            //noinspection SpellCheckingInspection
            player.sendMessage("§4Z§combie§4W§carrior §6| 僵尸战士");
            //noinspection SpellCheckingInspection
            player.sendMessage("§4S§ckeleton§4A§crcher §6| 骷髅弓箭手");
            //noinspection SpellCheckingInspection
            player.sendMessage("§4D§cark§4K§cnight §6| 黑暗骑士");
            //noinspection SpellCheckingInspection
            player.sendMessage("§4O§cre§4Z§combie §6| 矿石僵尸");
            //noinspection SpellCheckingInspection
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

            final Location loc = block.getLocation();
            loc.setY(loc.getY() + 1.0);
            switch (args[0].toLowerCase()) {
                case "zombiewarrior":
                case "zw":
                    new ZombieWarrior(namespacedKey).spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成僵尸战士.");
                    break;
                case "skeletonarcher":
                case "sa":
                    new SkeletonArcher(namespacedKey).spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成骷髅弓箭手.");
                    break;
                case "darkknight":
                case "dk":
                    new DarkKnight(namespacedKey).spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成黑暗骑士.");
                    break;
                case "orezombie":
                case "oz":
                    new OreZombie(namespacedKey).spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成矿石僵尸.");
                    break;
                case "blockzombie":
                case "bz":
                    new BlockZombie(namespacedKey).spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成方块僵尸.");
                    break;
                default:
                    player.sendMessage("§7[§b豆渣子§7] §c无效的疯狂怪物种类.");
            }
            return true;
        }
        return false;
    }
}
