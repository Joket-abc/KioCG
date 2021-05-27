package com.kiocg.PlayOhTheDungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

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

        final String uuid = player.getUniqueId().toString();
        if ((blockKey + Utils.variable) % (1500L + Utils.playerRabbits.get(uuid) * 1500L) == 126L) {
            Utils.variable = System.currentTimeMillis();

            Utils.playerRabbitConfirm.put(uuid, blockKey);
            player.sendMessage(Utils.getConfirmMessage(blockKey));
        }
    }

    @EventHandler
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
    public void onPlayerBedEnter(final @NotNull PlayerBedEnterEvent e) {
        if ("KioCG_OhTheDungeon".equals(e.getPlayer().getWorld().getName())) {
            e.setCancelled(true);
        }
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

    // 怪物掉落货币
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player || new Random().nextInt(10) != 0) {
            return;
        }

        final ItemStack itemStack = new ItemStack(Material.BARRIER, 1);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("铜币").decoration(TextDecoration.ITALIC, false));
        itemStack.setItemMeta(itemMeta);

        e.getDrops().add(itemStack);
    }
}
