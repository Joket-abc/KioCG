package com.kiocg.PlayOhTheDungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
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
import java.util.*;

public class Utils {
    // 存储触发兔子洞的玩家UUID、待确认的blockKey
    public static final Map<String, Long> playerRabbitConfirm = new HashMap<>();

    // 存储触发兔子洞的玩家UUID、触发次数
    public static final Map<String, Integer> playerRabbits = new HashMap<>();
    // 存储进入过的兔子洞blockKey
    public static final List<Long> RabbitKeys = new ArrayList<>();

    // 获取确认提示信息
    public static @NotNull Component getConfirmMessage(final long blockKey) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize("§7[§9豆渣子§7] ")
                                        .append(Component.text("你在地上发现了一个兔子窝洞口. ", NamedTextColor.GRAY))
                                        .append(Component.text("[探头去看看]", NamedTextColor.GRAY, TextDecoration.BOLD)
                                                         .clickEvent(ClickEvent.runCommand("/rabbit " + blockKey)));
    }

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

    public static void TeleportDungeon(final @NotNull Player player) {
        final Random random = new Random();
        final int x = random.nextInt(9001) - 4500;
        final int z = random.nextInt(9001) - 4500;

        player.teleport(Objects.requireNonNull(Bukkit.getWorld("KioCG_OhTheDungeon")).getHighestBlockAt(x, z).getLocation());
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
        // 延时外获取背包内容拷贝
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
            playerInventory.setItem(i, itemStack);
        }

        if (!playerFile.delete()) {
            player.sendMessage("§a[§b豆渣子§a] §4出现内部错误, 请联系管理员! (saveFile delete failed)");
        }
    }
}
