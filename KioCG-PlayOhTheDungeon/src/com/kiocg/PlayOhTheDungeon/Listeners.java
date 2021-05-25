package com.kiocg.PlayOhTheDungeon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Material material = e.getBlock().getType();
        if (material != Material.STONE) {
            // 拿着刷怪笼打破基岩可以触发兔子洞
            if (material == Material.BEDROCK) {
                final Player player = e.getPlayer();
                final Material handItemMaterial = player.getInventory().getItemInMainHand().getType();
                if (handItemMaterial == Material.SPAWNER) {
                    Utils.joinRabbit(player);
                }
            }

            return;
        }

        final Player player = e.getPlayer();

        if (player.getLocation().getBlockY() > 64) {
            return;
        }

        final String uuid = player.getUniqueId().toString();

        // 兔子洞触发的倒数
        if (!Utils.playerRabbits.containsKey(uuid)) {
            Utils.playerRabbits.put(uuid, new Random().nextInt(7000) + 3000);
            return;
        } else {
            final int rabbits = Utils.playerRabbits.get(uuid);

            if (rabbits > 0) {
                Utils.playerRabbits.put(uuid, rabbits - 1);
                return;
            }
        }

        Utils.joinRabbit(player);
        Utils.playerRabbits.put(uuid, new Random().nextInt(25000) + 5000);
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
    public void onPlayerDeath(final @NotNull PlayerDeathEvent e) {
        final Player player = e.getEntity();

        if (!"KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            return;
        }

        player.sendMessage("§a[§b豆渣子§a] §2你醒了... 可是为什么你在这?");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        if (!"KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            return;
        }

        player.setHealth(0.0);
    }
}
