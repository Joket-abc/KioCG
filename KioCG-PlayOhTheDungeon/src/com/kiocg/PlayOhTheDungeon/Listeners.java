package com.kiocg.PlayOhTheDungeon;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        //TODO 大版本更新时的世界名修改
        if (!"KioCG_17world".equals(block.getWorld().getName())) {
            return;
        }

        // 拿着石头打破基岩可以触发兔子洞
        if (block.getType() == Material.BEDROCK) {
            final Player player = e.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() == Material.STONE) {
                final long blockKey = Utils.getBlockKey(block.getLocation());
                Utils.playerRabbitConfirm.put(player.getUniqueId().toString(), blockKey);
                player.spigot().sendMessage(Utils.getConfirmMessage(blockKey));
                return;
            }
        }

        final Player player = e.getPlayer();
        final String uuidString = player.getUniqueId().toString();

        final long blockKey = Utils.getBlockKey(block.getLocation());
        if (blockKey % (1000L + Utils.playerRabbits.get(uuidString) * 1000L) == Utils.variable) {
            Utils.variable = System.currentTimeMillis() % 1000L;

            Utils.playerRabbitConfirm.put(uuidString, blockKey);
            player.spigot().sendMessage(Utils.getConfirmMessage(blockKey));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final String uuidString = e.getPlayer().getUniqueId().toString();
        if (!Utils.playerRabbits.containsKey(uuidString)) {
            Utils.playerRabbits.put(uuidString, 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangedWorld(final @NotNull PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();

        if ("KioCG_OhTheDungeon".equals(e.getFrom().getName())) {
            Utils.restoreBackpackAndLoot(e.getPlayer());
            player.sendMessage("§a[§b豆渣子§a] §2你醒了... 可是为什么你在这?");
            return;
        }

        if ("KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            Utils.saveAndClearBackpack(player);
            player.sendTitle("", "§7... 怎么回事, 我这是在哪? ...", 10, 70, 20);
            player.sendMessage("§a[§b豆渣子§a] §3你来到了一个奇怪的世界,");
            player.sendMessage("§a[§b豆渣子§a] §2尝试找到回到现实世界的办法吧...");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = e.getClickedBlock();
        final World world = Objects.requireNonNull(block).getWorld();

        if (!"KioCG_OhTheDungeon".equals(world.getName())) {
            return;
        }

        final Material material = block.getType();
        if (material.toString().endsWith("_BED")) {
            world.createExplosion(block.getLocation(), 5.0F, true, true);
            e.setCancelled(true);
        } else if (material == Material.ENDER_CHEST || material == Material.LODESTONE) {
            e.setCancelled(true);
        }
    }
}
