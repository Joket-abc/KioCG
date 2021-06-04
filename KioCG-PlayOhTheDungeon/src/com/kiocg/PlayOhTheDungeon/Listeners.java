package com.kiocg.PlayOhTheDungeon;

import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        //TODO 大版本更新时的世界名修改
        if (!"KioCG_world".equals(block.getWorld().getName())) {
            return;
        }

        // 拿着刷怪笼打破基岩可以触发兔子洞
        if (block.getType() == Material.BEDROCK) {
            final Player player = e.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() == Material.SPAWNER) {
                final long blockKey = block.getBlockKey();
                Utils.playerRabbitConfirm.put(player.getUniqueId().toString(), blockKey);
                player.sendMessage(Utils.getConfirmMessage(blockKey));
                return;
            }
        }

        final Player player = e.getPlayer();
        final String uuidString = player.getUniqueId().toString();

        final long blockKey = block.getBlockKey();
        if (blockKey % (1000L + Utils.playerRabbits.get(uuidString) * 1000L) == Utils.variable) {
            Utils.variable = System.currentTimeMillis() % 1000L;

            Utils.playerRabbitConfirm.put(uuidString, blockKey);
            player.sendMessage(Utils.getConfirmMessage(blockKey));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        final String uuidString = player.getUniqueId().toString();
        if (!Utils.playerRabbits.containsKey(uuidString)) {
            Utils.playerRabbits.put(uuidString, 0);
        }

        if ("KioCG_OhTheDungeon".equals(player.getWorld().getName())) {
            player.setHealth(0.0);
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

        if (!"KioCG_OhTheDungeon".equals(Objects.requireNonNull(block).getWorld().getName())) {
            return;
        }

        final Material material = block.getType();
        if (MaterialTags.BEDS.isTagged(material) || material == Material.ENDER_CHEST) {
            block.getLocation().createExplosion(5.0F, true, true);
            e.setCancelled(true);
        }
    }

    // 怪物掉落货币
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final Entity entity = e.getEntity();

        if (!"KioCG_OhTheDungeon".equals(entity.getWorld().getName()) || entity instanceof Player || new Random().nextInt(7) != 0) {
            return;
        }

        final ItemStack itemStack = new ItemStack(Material.BARRIER, 1);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("铜币").decoration(TextDecoration.ITALIC, false));
        itemStack.setItemMeta(itemMeta);

        e.getDrops().add(itemStack);
    }
}
