package com.kiocg.WoolTree;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
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

        if (!MaterialSetTag.SAPLINGS.isTagged(Objects.requireNonNull(block).getType())) {
            return;
        }

        final Player player = e.getPlayer();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!MaterialTags.DYES.isTagged(itemStack.getType())) {
            return;
        }

        if (!player.hasPermission("kiocg.wooltree.ues")) {
            player.sendMessage("§a[§b豆渣子§a] §6你不可以种植羊毛树.");
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }

        final Location location = block.getLocation().toCenterLocation();
        if (new Random().nextInt() * 100 >= 45) {
            block.getWorld().playSound(location, Sound.BLOCK_COMPOSTER_FILL, 1.0F, 1.0F);
            block.getWorld().playEffect(location.add(0.0, 1.0, 0.0), Effect.SMOKE, 0);
            return;
        }

        final long blockKey = block.getBlockKey();
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

        if (MaterialSetTag.SAPLINGS.isTagged(Objects.requireNonNull(block).getType())) {
            Utils.treeWools.remove(block.getBlockKey());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onStructureGrow(final @NotNull StructureGrowEvent e) {
        final long blockKey = e.getLocation().toBlockKey();

        if (!Utils.treeWools.containsKey(blockKey)) {
            return;
        }

        final List<Material> wools = Utils.treeWools.get(blockKey);

        final Random random = new Random();
        final int amount = wools.size();
        for (final BlockState blockState : e.getBlocks()) {
            if (MaterialSetTag.LEAVES.isTagged(blockState.getType())) {
                blockState.setType(wools.get(random.nextInt(amount)));
            }
        }
    }
}
