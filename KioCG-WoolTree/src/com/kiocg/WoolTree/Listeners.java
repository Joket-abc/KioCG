package com.kiocg.WoolTree;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || Objects.requireNonNull(e.getHand()) != EquipmentSlot.HAND) {
            return;
        }

        final Block block = e.getClickedBlock();

        switch (Objects.requireNonNull(block).getType()) {
            case ACACIA_SAPLING:
            case BIRCH_SAPLING:
            case DARK_OAK_SAPLING:
            case JUNGLE_SAPLING:
            case OAK_SAPLING:
            case SPRUCE_SAPLING:
                break;
            default:
                return;
        }

        final Player player = e.getPlayer();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!itemStack.getType().toString().endsWith("_DYE")) {
            return;
        }

        if (!player.hasPermission("kiocg.wooltree.ues")) {
            player.sendMessage("§a[§b豆渣子§a] §6你不可以种植羊毛树.");
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }

        final Location location = block.getLocation().add(0.5, 0.5, 0.5);
        if (new Random().nextInt() * 100 >= 45) {
            block.getWorld().playSound(location, Sound.BLOCK_COMPOSTER_FILL, 1.0F, 1.0F);
            block.getWorld().playEffect(location.add(0.0, 1.0, 0.0), Effect.SMOKE, 0);
            return;
        }

        final long blockKey = Utils.getBlockKey(block.getLocation());
        if (Utils.treeWools.containsKey(blockKey)) {
            Utils.treeWools.get(blockKey).add(Utils.dye2Wool(itemStack.getType()));
        } else {
            Utils.treeWools.put(blockKey, new ArrayList<>() {{
                add(Utils.dye2Wool(itemStack.getType()));
            }});
        }

        block.getWorld().playSound(location, Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1.0F, 1.0F);
        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 9, 0.1, 0.1, 0.1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        switch (Objects.requireNonNull(block).getType()) {
            case ACACIA_SAPLING, BIRCH_SAPLING, DARK_OAK_SAPLING, JUNGLE_SAPLING, OAK_SAPLING, SPRUCE_SAPLING -> Utils.treeWools.remove(Utils.getBlockKey(block.getLocation()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onStructureGrow(final @NotNull StructureGrowEvent e) {
        final long blockKey = Utils.getBlockKey(e.getLocation());

        final List<Material> wools = Utils.treeWools.get(blockKey);

        if (wools == null) {
            return;
        }

        final Random random = new Random();
        final int amount = wools.size();
        for (final BlockState blockState : e.getBlocks()) {
            if (blockState.getType().toString().endsWith("_LEAVES")) {
                blockState.setType(wools.get(random.nextInt(amount)));
            }
        }

        Utils.treeWools.remove(blockKey);
    }
}
