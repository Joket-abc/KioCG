package com.kiocg.LittleThings.listeners.Misc;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class cropHoe implements Listener {
    // 锄头才是采集作物的正确工具
    @EventHandler
    public void cropHoe(final @NotNull BlockDropItemEvent e) {
        if (!MaterialSetTag.CROPS.isTagged(e.getBlockState().getType())
            || MaterialTags.HOES.isTagged(e.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }

        e.getItems().forEach(item -> {
            final ItemStack itemStack = item.getItemStack();
            itemStack.setAmount(Math.min(5, itemStack.getAmount()));
        });
    }
}
