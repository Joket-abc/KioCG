package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.DarkKnight;
import com.kiocg.InsaneMonsters.mobs.SkeletonArcher;
import com.kiocg.InsaneMonsters.mobs.ZombieWarrior;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class InsaneMonsters extends JavaPlugin implements Listener {
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "InsaneMonsters");
    private final ZombieWarrior zombieWarrior = new ZombieWarrior(namespacedKey);
    private final SkeletonArcher skeletonArcher = new SkeletonArcher(namespacedKey);
    private final DarkKnight darkKnight = new DarkKnight(namespacedKey);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }

        LivingEntity livingEntity = e.getEntity();
        final Random random = new Random();
        switch (livingEntity.getType()) {
            case ZOMBIE:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = zombieWarrior.spawn(livingEntity.getLocation());
                break;
            case SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = skeletonArcher.spawn(livingEntity.getLocation());
                break;
            case WITHER_SKELETON:
                if (random.nextInt(100) < 95) {
                    return;
                }

                livingEntity = darkKnight.spawn(livingEntity.getLocation());
                break;
            default:
                return;
        }
        livingEntity.setCustomName("§cSCP-" + String.format("%03d", random.nextInt(6000)));
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent e) {
        final String persistentData = e.getEntity().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        if (persistentData == null) {
            return;
        }

        final List<ItemStack> drops = e.getDrops();
        final Random random = new Random();
        switch (persistentData) {
            case "ZombieWarrior":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.ROTTEN_FLESH, random.nextInt(3) + 1));
                break;
            case "SkeletonArcher":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                drops.add(new ItemStack(Material.BONE, random.nextInt(3) + 1));
                break;
            case "DarkKnight":
                drops.clear();
                drops.add(new ItemStack(Material.DIAMOND, random.nextInt(3) + 1));
                if (random.nextInt(100) < 10) {
                    drops.add(new ItemStack(Material.NETHERITE_SCRAP));
                }
                break;
        }
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
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
                    zombieWarrior.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成僵尸战士.");
                    break;
                case "skeletonarcher":
                case "sa":
                    skeletonArcher.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成骷髅弓箭手.");
                    break;
                case "darkknight":
                case "dk":
                    darkKnight.spawn(loc);
                    player.sendMessage("§a[§b豆渣子§a] §6已生成黑暗骑士.");
                    break;
                default:
                    player.sendMessage("§7[§b豆渣子§7] §c无效的疯狂怪物种类.");
            }
            return true;
        }
        return false;
    }
}
