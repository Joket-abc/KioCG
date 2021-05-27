package com.kiocg.WoolTree;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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

        final ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();

        if (!MaterialTags.DYES.isTagged(itemStack.getType())) {
            return;
        }

        if (new Random().nextInt() * 100 >= 45) {
            final Location location = block.getLocation().toCenterLocation().add(0.0, 1.0, 0.0);
            block.getWorld().playEffect(location, Effect.COMPOSTER_COMPOSTS, 0);
            block.getWorld().playEffect(location, Effect.SMOKE, 0);
            itemStack.setAmount(itemStack.getAmount() - 1);
            return;
        }

        final long blockKey = block.getBlockKey();
        if (Utils.treeWools.containsKey(blockKey)) {
            Utils.treeWools.get(blockKey).add(Utils.dye2wool(itemStack.getType()));
        } else {
            Utils.treeWools.put(blockKey, new ArrayList<>() {{
                add(Utils.dye2wool(itemStack.getType()));
            }});
        }

        final Location location = block.getLocation().toCenterLocation();
        block.getWorld().playEffect(location, Effect.COMPOSTER_COMPOSTS, 0);
        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 9, 0.1, 0.1, 0.1);

        itemStack.setAmount(itemStack.getAmount() - 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final @NotNull BlockBreakEvent e) {
        final Block block = e.getBlock();

        if (!MaterialSetTag.SAPLINGS.isTagged(Objects.requireNonNull(block).getType())) {
            return;
        }

        Utils.treeWools.remove(block.getBlockKey());
    }

    @EventHandler(ignoreCancelled = true)
    public void onStructureGrow(final @NotNull StructureGrowEvent e) {
        final long blockKey = e.getLocation().toBlockKey();

        if (!Utils.treeWools.containsKey(blockKey)) {
            return;
        }

        final List<Material> wools = Utils.treeWools.get(blockKey);
        final int amount = wools.size();
        final Random random = new Random();
        for (final BlockState blockState : e.getBlocks()) {
            if (MaterialSetTag.LEAVES.isTagged(blockState.getType())) {
                blockState.setType(wools.get(random.nextInt(amount)));
            }
        }
    }
}
