package com.kiocg.LittleThings.listeners;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Misc implements @NotNull Listener {
    // 死亡原地复活
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(final @NotNull PlayerRespawnEvent e) {
        final Player player = e.getPlayer();
        final Location loc = player.getLocation();

        //TODO 1.17 Y坐标更改!
        if (loc.getY() < 0.0) {
            return;
        }

        final World.Environment environment = loc.getWorld().getEnvironment();

        if (environment.equals(World.Environment.NETHER) && loc.getY() > 127.0) {
            return;
        }

        if (environment.equals(World.Environment.THE_END) && loc.getBlock().getType().equals(Material.END_PORTAL)) {
            return;
        }

        e.setRespawnLocation(loc);

        final int foodLevel = player.getFoodLevel();
        final float saturation = player.getSaturation();
        final float exhaustion = player.getExhaustion();

        Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
            player.setFoodLevel(foodLevel);
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
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(e.message());

        if (!message.contains("@") || !e.getPlayer().hasPermission("kiocg.littlethings.at")) {
            return;
        }

        // 获取在线玩家名列表，从长到短排序
        final List<String> onlinePlayersName = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayersName.add(player.getName());
        }

        onlinePlayersName.sort((a, b) -> (b.length() - a.length()));

        for (final String playerName : onlinePlayersName) {
            if (message.toLowerCase().contains("@" + playerName.toLowerCase()) || message.toLowerCase().contains("@ " + playerName.toLowerCase())) {
                final Player thePlayer = Bukkit.getPlayer(playerName);
                Objects.requireNonNull(thePlayer).playSound(thePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);

                message = message.replaceAll("(?i)@" + playerName, "§9§o@§9§o" + playerName + "§r")
                                 .replaceAll("(?i)@ " + playerName, "§9§o@§9§o" + playerName + "§r");
            }
        }

        e.message(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    // 随身工作台
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final @NotNull InventoryClickEvent e) {
        try {
            if (!Objects.requireNonNull(e.getClickedInventory()).getType().equals(InventoryType.CRAFTING)) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (!e.getSlotType().equals(InventoryType.SlotType.RESULT)
            || !Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.AIR)
            || !Objects.requireNonNull(e.getCursor()).getType().equals(Material.AIR)) {
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
}
