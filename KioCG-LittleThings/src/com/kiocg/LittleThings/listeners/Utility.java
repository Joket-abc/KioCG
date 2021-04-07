package com.kiocg.LittleThings.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utility implements @NotNull Listener {
    // 无法放置
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        try {
            if (Objects.requireNonNull(e.getItemInHand().lore()).contains(LegacyComponentSerializer.legacyAmpersand().deserialize("§9无法放置"))) {
                e.setCancelled(true);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 生物掉落矿物特有标签
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Mob)) {
            return;
        }

        for (final ItemStack itemStack : e.getDrops()) {
            switch (itemStack.getType()) {
                case COAL:
                case IRON_INGOT:
                case GOLD_INGOT:
                case GOLD_NUGGET:
                case REDSTONE:
                case EMERALD:
                    List<Component> lore = itemStack.lore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }

                    lore.add(Component.text("(生物掉落)", NamedTextColor.GRAY));

                    itemStack.lore(lore);
            }
        }
    }

    // 生物掉落矿物合成标签
    @EventHandler
    public void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent e) {
        final CraftingInventory craftingInventory = e.getInventory();
        final ItemStack result = craftingInventory.getResult();

        if (result == null) {
            return;
        }

        for (final ItemStack itemStack : craftingInventory.getMatrix()) {
            try {
                for (final Component lore : Objects.requireNonNull(itemStack.lore())) {
                    if ("(生物掉落)".equals(PlainComponentSerializer.plain().serialize(lore))) {
                        result.lore(new ArrayList<>() {{
                            add(Component.text("(生物掉落)", NamedTextColor.GRAY));
                        }});

                        return;
                    }
                }
            } catch (final @NotNull NullPointerException ignore) {
            }
        }
    }
}
