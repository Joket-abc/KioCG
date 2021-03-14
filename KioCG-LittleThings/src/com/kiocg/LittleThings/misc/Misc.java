package com.kiocg.LittleThings.misc;

import com.kiocg.LittleThings.LittleThings;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Misc implements Listener {
    // 死亡原地复活
    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent e) {
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

        e.setRespawnLocation(player.getLocation());
        new BukkitRunnable() {
            final int foodLevel = player.getFoodLevel();
            final float saturation = player.getSaturation();

            @Override
            public void run() {
                player.setFoodLevel(foodLevel);
                player.setSaturation(saturation);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 15, 9));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 15, 9));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 9));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 15, 9));
            }
        }.runTask(LittleThings.getInstance());
    }

    // @玩家
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(final AsyncChatEvent e) {
        String message = PlainComponentSerializer.plain().serialize(e.message());
        if (!message.contains("@")) {
            return;
        }
        if (!e.getPlayer().hasPermission("kiocg.at.use")) {
            return;
        }

        // 获取在线玩家名列表，从长到短排序
        final List<String> playersName = new ArrayList<>();
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            playersName.add(player.getName().toLowerCase());
        }
        playersName.sort((a, b) -> (b.length() - a.length()));

        for (final String playerName : playersName) {
            if (message.toLowerCase().contains("@" + playerName) || message.toLowerCase().contains("@ " + playerName)) {
                final Player thePlayer = Bukkit.getServer().getPlayer(playerName);
                Objects.requireNonNull(thePlayer).playSound(thePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                message = message.replaceAll("(?i)@" + playerName, "§9§o@§9§o" + playerName + "§r").replaceAll("(?i)@ " + playerName, "§9§o@§9§o" + playerName + "§r");
            }
        }
        e.message(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    // 随身工作台
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().getType().equals(InventoryType.CRAFTING) || !e.getSlotType().equals(InventoryType.SlotType.RESULT)
                || (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        final Player player = (Player) e.getWhoClicked();
        if (player.hasPermission("kiocg.littlethings.fastworkbench")) {
            Bukkit.getServer().getScheduler().runTask(LittleThings.getInstance(), () -> player.openWorkbench(player.getLocation(), true));
        }
    }
}
