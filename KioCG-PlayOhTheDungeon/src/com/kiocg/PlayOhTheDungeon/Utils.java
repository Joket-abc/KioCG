package com.kiocg.PlayOhTheDungeon;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Utils {
    // 存储触发兔子洞的玩家UUID、剩余触发次数
    public static final Map<String, Integer> playerRabbits = new HashMap<>();

    public static void joinRabbit(final @NotNull Player player) {
        new BukkitRunnable() {
            int i;

            @Override
            public void run() {
                switch (i) {
                    case 0 -> player.sendTitle("", "§7... 你感到头晕目眩 ...", 10, 70, 20);

                    case 1 -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 99999, 9));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 99999, 255));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 99999, 9));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 0));
                    }
                    case 2 -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 99999, 9));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 1));
                    }
                    case 3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 2));
                    case 4 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 3));
                    case 5 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 4));
                    case 6 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 5));
                    case 7 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 6));
                    case 8 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 99999, 7));

                    case 9 -> TeleportDungeon(player);

                    case 10 -> {
                        player.sendTitle("", "§7... 怎么回事, 我这是在哪? ...", 10, 70, 20);
                        player.sendMessage("§a[§b豆渣子§a] §3你来到了一个梦境中的地牢世界,");
                        player.sendMessage("§a[§b豆渣子§a] §2尝试找到回到现实世界的办法吧...");
                        player.removePotionEffect(PotionEffectType.CONFUSION);
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.removePotionEffect(PotionEffectType.LEVITATION);
                        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        cancel();
                    }
                }

                ++i;
            }
        }.runTaskTimer(PlayOhTheDungeon.instance, 0L, 10L);
    }

    public static void saveAndClearBackpack(final @NotNull Player player) {
        final File playerFile = new File(PlayOhTheDungeon.instance.getDataFolder(), player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile exists)");
            return;
        }
        try {
            if (!playerFile.createNewFile()) {
                player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile create failed)");
                return;
            }
        } catch (final @NotNull IOException e) {
            player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile create IOException)");
            e.printStackTrace();
            return;
        }

        final PlayerInventory playerInventory = player.getInventory();

        final FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(playerFile);
        for (int i = 0; i < 41; ++i) {
            final ItemStack itemStack = playerInventory.getItem(i);
            if (itemStack != null) {
                fileConfiguration.set(String.valueOf(i), itemStack);
            }
        }
        try {
            fileConfiguration.save(playerFile);
        } catch (final @NotNull IOException e) {
            e.printStackTrace();
        }

        playerInventory.clear();
    }

    public static void restoreBackpackAndLoot(final @NotNull Player player) {
        final File playerFile = new File(PlayOhTheDungeon.instance.getDataFolder(), player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile not exists)");
            return;
        }

        final PlayerInventory playerInventory = player.getInventory();
        // 延时外获取背包内容
        final ItemStack[] contents = playerInventory.getContents();
        playerInventory.clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (final ItemStack itemStack : contents) {
                    if (itemStack != null) {
                        player.getWorld().dropItem(player.getLocation(), itemStack);
                    }
                }
            }
        }.runTaskLater(PlayOhTheDungeon.instance, 10L);

        for (int i = 0; i < 41; ++i) {
            final ItemStack itemStack = (ItemStack) YamlConfiguration.loadConfiguration(playerFile).get(String.valueOf(i));
            if (itemStack != null) {
                playerInventory.setItem(i, itemStack);
            }
        }

        if (!playerFile.delete()) {
            player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile delete failed)");
        }
    }

    public static void TeleportDungeon(final @NotNull Player player) {
        final World world = Bukkit.getWorld("KioCG_OhTheDungeon");

        final Random random = new Random();
        final int x = random.nextInt(9001) - 4500;
        final int z = random.nextInt(9001) - 4500;

        player.teleport(Objects.requireNonNull(world).getHighestBlockAt(x, z).getLocation());
    }
}
