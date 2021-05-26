package com.kiocg.PlayOhTheDungeon;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        // 拿着刷怪笼打破基岩可以触发兔子洞
        if (block.getType() == Material.BEDROCK) {
            final Player player = e.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() == Material.SPAWNER) {
                Utils.joinRabbit(player);
            }
        }

        final Player player = e.getPlayer();

        final long blockKey = block.getBlockKey();
        if (Utils.RabbitKeys.contains(blockKey)) {
            return;
        }

        if (blockKey % (3000L + Utils.playerRabbits.get(player.getUniqueId().toString()) * 3000L) == 126L) {
            Utils.RabbitKeys.add(blockKey);
            Utils.joinRabbit(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(final @NotNull PlayerChangedWorldEvent e) {
        if ("KioCG_OhTheDungeon".equals(e.getFrom().getName())) {
            Utils.restoreBackpackAndLoot(e.getPlayer());
            return;
        }

        final Player player = e.getPlayer();
        if ("KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            Utils.saveAndClearBackpack(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnter(final @NotNull PlayerBedEnterEvent e) {
        if ("KioCG_OhTheDungeon".equals(e.getPlayer().getWorld().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(final @NotNull PlayerDeathEvent e) {
        final Player player = e.getEntity();

        if (!"KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §2你醒了... 可是为什么你在这?");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        final String uuid = player.getUniqueId().toString();
        if (!Utils.playerRabbits.containsKey(uuid)) {
            Utils.playerRabbits.put(uuid, 0);
        }

        if (!"KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            return;
        }

        player.setHealth(0.0);
    }
}
