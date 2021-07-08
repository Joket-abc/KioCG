package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class cropHoe implements Listener {
    private final Tag<Material> crops = Bukkit.getTag("blocks", NamespacedKey.minecraft("crops"), Material.class);

    // 锄头才是采集作物的正确工具
    @EventHandler
    public void cropHoe(final @NotNull BlockDropItemEvent e) {
        if (!crops.isTagged(e.getBlockState().getType())
            || e.getPlayer().getInventory().getItemInMainHand().getType().toString().endsWith("_HOE")) {
            return;
        }

        e.getItems().forEach(item -> {
            final ItemStack itemStack = item.getItemStack();
            itemStack.setAmount(Math.min(5, itemStack.getAmount()));
        });
    }
}
