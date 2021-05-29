package com.kiocg.LittleThings.listeners;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Misc implements Listener {
    // 死亡原地复活
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(final @NotNull PlayerRespawnEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.originrespawn.bypass")) {
            return;
        }

        final Location loc = player.getLocation();

        //TODO 1.17 Y坐标更改!
        if (loc.getY() < 0.0) {
            return;
        }

        final String worldName = loc.getWorld().getName();
        if (("KioCG_world_nether".equals(worldName) && loc.getY() > 127.0)
            || ("KioCG_world_the_end".equals(worldName) && loc.getBlock().getType() == Material.END_PORTAL)
            || "KioCG_OhTheDungeon".equals(worldName)) {
            return;
        }

        e.setRespawnLocation(loc);

        final int foodLevel = player.getFoodLevel();
        final float saturation = player.getSaturation();
        final float exhaustion = player.getExhaustion();
        Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
            player.setFoodLevel(Math.max(foodLevel, 3));
            player.setSaturation(saturation);
            player.setExhaustion(exhaustion);

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 9, 9));
        });
    }

    // @玩家
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(final @NotNull AsyncChatEvent e) {
        Component message = e.message();
        final String messageString = message.toString();

        if (!messageString.contains("@") || !e.getPlayer().hasPermission("kiocg.littlethings.at")) {
            return;
        }

        // 获取在线玩家名列表，从长到短排序
        final List<String> onlinePlayersName = new ArrayList<>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayersName.add(player.getName());
        }
        onlinePlayersName.sort((a, b) -> (b.length() - a.length()));

        final String messageStringLowerCase = messageString.toLowerCase();
        for (final String playerName : onlinePlayersName) {
            final String playerNameLowerCase = playerName.toLowerCase();
            if (messageStringLowerCase.contains("@ " + playerNameLowerCase) || messageStringLowerCase.contains("@" + playerNameLowerCase)) {
                final Player thePlayer = Bukkit.getPlayer(playerName);
                Objects.requireNonNull(thePlayer).playSound(thePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);

                message = message.replaceText(TextReplacementConfig.builder().match("(?i)@ " + playerNameLowerCase).replacement("§9§o@§9§o" + playerName + "§r").build())
                                 .replaceText(TextReplacementConfig.builder().match("(?i)@" + playerNameLowerCase).replacement("§9§o@§9§o" + playerName + "§r").build());
            }
        }

        e.message(message);
    }

    // 随身工作台
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final @NotNull InventoryClickEvent e) {
        try {
            if (Objects.requireNonNull(e.getClickedInventory()).getType() != InventoryType.CRAFTING) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (e.getSlotType() != InventoryType.SlotType.RESULT || Objects.requireNonNull(e.getCurrentItem()).getType() != Material.AIR) {
            return;
        }

        final Player player = (Player) e.getWhoClicked();

        if (player.hasPermission("kiocg.littlethings.fastworkbench")) {
            Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
                player.closeInventory();
                player.openWorkbench(player.getLocation(), true);
            });

            e.setCancelled(true);
        }
    }

    // 随机放置方块
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        final PlayerInventory playerInventory = e.getPlayer().getInventory();

        if (playerInventory.getItemInOffHand().getType() != Material.STICK) {
            return;
        }

        final List<Integer> slots = new ArrayList<>();
        for (int i = 0; i <= 8; ++i) {
            final ItemStack itemStack = playerInventory.getItem(i);
            if (itemStack != null && itemStack.getType().isBlock()) {
                slots.add(i);
            }
        }

        playerInventory.setHeldItemSlot(slots.get(new Random().nextInt(slots.size())));
    }
}
