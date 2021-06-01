package com.kiocg.InfiniteEnchant;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPrepareAnvilBook(final @NotNull PrepareAnvilEvent e) {
        final ItemStack item3 = e.getResult();
        final ItemMeta itemMeta3;
        try {
            itemMeta3 = Objects.requireNonNull(item3).getItemMeta();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final AnvilInventory anvilInventory = e.getInventory();

        final ItemStack item1 = anvilInventory.getFirstItem();
        final ItemStack item2 = anvilInventory.getSecondItem();
        final ItemMeta itemMeta1;
        final ItemMeta itemMeta2;
        try {
            itemMeta1 = Objects.requireNonNull(item1).getItemMeta();
            itemMeta2 = Objects.requireNonNull(item2).getItemMeta();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        // 获取铁砧合成栏中两个物品的附魔属性
        final Map<Enchantment, Integer> enchantments1;
        final Map<Enchantment, Integer> enchantments2;

        if (item1.getType() == Material.ENCHANTED_BOOK) {
            enchantments1 = ((EnchantmentStorageMeta) itemMeta1).getStoredEnchants();
        } else {
            enchantments1 = itemMeta1.getEnchants();
        }

        if (item2.getType() == Material.ENCHANTED_BOOK) {
            enchantments2 = ((EnchantmentStorageMeta) itemMeta2).getStoredEnchants();
        } else {
            enchantments2 = itemMeta2.getEnchants();
        }

        // 获取待合成物品上完全相同的附魔属性
        final Map<Enchantment, Integer> enchantEquals = new HashMap<>();
        // 获取待合成物品上所有的附魔属性, 并保留等级较大的
        final Map<Enchantment, Integer> enchantBigger = new HashMap<>(enchantments2);

        enchantments1.forEach((enchantment, level) -> {
            if (enchantments2.containsKey(enchantment) && enchantments2.get(enchantment).equals(level)) {
                enchantEquals.put(enchantment, level);
            }
            enchantBigger.merge(enchantment, level, Integer::max);
        });

        // 获取铁砧结果栏中物品的附魔属性
        final Map<Enchantment, Integer> enchantments3;
        final boolean item3IsBook;

        if (item3.getType() == Material.ENCHANTED_BOOK) {
            enchantments3 = ((EnchantmentStorageMeta) itemMeta3).getStoredEnchants();
            item3IsBook = true;
        } else {
            enchantments3 = itemMeta3.getEnchants();
            item3IsBook = false;
        }

        // enchantBigger中去除结果物品没有的附魔属性
        enchantBigger.keySet().removeIf(key -> !enchantments3.containsKey(key));

        // 创建结果附魔
        final Map<Enchantment, Integer> enchantResult = new HashMap<>();

        enchantEquals.forEach((enchantmentEquals, levelEquals) -> {
            // 判断结果物品上的附魔属性是否小于等于待合成物品(到达原版附魔等级上限)
            if (enchantments3.get(enchantmentEquals) <= levelEquals) {
                enchantResult.put(enchantmentEquals, levelEquals < 9 ? ++levelEquals : 10);
            }
            enchantBigger.remove(enchantmentEquals);
        });

        // 输出结果物品
        enchantResult.putAll(enchantBigger);

        if (item3IsBook) {
            enchantResult.forEach((enchantment, level) -> {
                ((EnchantmentStorageMeta) itemMeta3).removeStoredEnchant(enchantment);
                ((EnchantmentStorageMeta) itemMeta3).addStoredEnchant(enchantment, level, true);
            });
        } else {
            enchantResult.forEach((enchantment, level) -> {
                itemMeta3.removeEnchant(enchantment);
                itemMeta3.addEnchant(enchantment, level, true);
            });
        }

        item3.setItemMeta(itemMeta3);
        e.setResult(item3);
    }

    @EventHandler
    public void onReduceRepairCost(final @NotNull PrepareAnvilEvent e) {
        final ItemStack item3 = e.getResult();
        final ItemMeta itemMeta3;
        try {
            itemMeta3 = Objects.requireNonNull(item3).getItemMeta();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        final AnvilInventory anvilInventory = e.getInventory();

        final ItemStack item1 = anvilInventory.getFirstItem();
        final ItemStack item2 = anvilInventory.getSecondItem();
        final Material itemMaterial1;
        final Material itemMaterial2;
        try {
            itemMaterial1 = Objects.requireNonNull(item1).getType();
            itemMaterial2 = Objects.requireNonNull(item2).getType();
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        if (itemMaterial1 != Material.ENCHANTED_BOOK && itemMaterial1 == itemMaterial2 && !item1.getItemMeta().hasEnchants()) {
            final int repairCost = ((Repairable) itemMeta3).getRepairCost();
            if (repairCost < 3) {
                e.setResult(null);
                return;
            }

            ((Repairable) itemMeta3).setRepairCost((repairCost - 3) >> 2);

            item3.setItemMeta(itemMeta3);
            e.setResult(item3);
        } else if (itemMaterial1 == Material.BOOK) {
            final int repairCost = ((Repairable) itemMeta3).getRepairCost();
            if (repairCost < 3) {
                e.setResult(null);
                return;
            }

            final ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
            final ItemMeta enchantedBookMeta = enchantedBook.getItemMeta();

            ((Repairable) enchantedBookMeta).setRepairCost((repairCost - 3) >> 2);

            final EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) enchantedBookMeta;
            itemMeta3.getEnchants().forEach((enchantment, level) -> enchantmentStorageMeta.addStoredEnchant(enchantment, level, true));

            enchantedBook.setItemMeta(enchantedBookMeta);
            e.setResult(enchantedBook);
        }
    }

    //TODO 权限判断 kiocg.infiniteenchant.use
}
