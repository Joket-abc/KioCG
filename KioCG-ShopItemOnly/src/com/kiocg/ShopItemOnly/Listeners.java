package com.kiocg.ShopItemOnly;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.shop.Shop;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(final @NotNull InventoryClickEvent e) {
        final ItemStack itemStack = e.getCurrentItem();

        final Material material;
        try {
            material = Objects.requireNonNull(itemStack).getType();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }
        if (material == Material.AIR || material == Material.COBBLESTONE) {
            return;
        }

        final QuickShop quickShop = QuickShop.getInstance();
        final Shop shop = quickShop.getShopManager().getShopIncludeAttached(e.getView().getTopInventory().getLocation());

        if (shop == null) {
            return;
        }

        if (!quickShop.getItemMatcher().matches(shop.getItem(), itemStack)) {
            e.getWhoClicked().sendMessage("§a[§b豆渣子§a] §6你只能在商店里操作商品或原石(用于占位).");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryMoveItem(final @NotNull InventoryMoveItemEvent e) {
        final QuickShop quickShop = QuickShop.getInstance();
        final Shop shop = quickShop.getShopManager().getShopIncludeAttached(e.getDestination().getLocation());

        if (shop == null) {
            return;
        }

        if (!quickShop.getItemMatcher().matches(shop.getItem(), e.getItem())) {
            e.setCancelled(true);
        }
    }
}
