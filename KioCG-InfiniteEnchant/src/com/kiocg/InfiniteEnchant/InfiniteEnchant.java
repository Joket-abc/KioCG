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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class InfiniteEnchant extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvilBook(final PrepareAnvilEvent e) {
        final ItemStack item3 = e.getResult();
        if (item3 == null) {
            return;
        }
        final AnvilInventory anvilInventory = e.getInventory();
        final ItemStack item1 = anvilInventory.getFirstItem();
        final ItemStack item2 = anvilInventory.getSecondItem();
        if (item2 == null || item1 == null) {
            return;
        }

        // 获取铁砧合成栏中两个物品的附魔属性
        final Map<Enchantment, Integer> enchantments1;
        final Map<Enchantment, Integer> enchantments2;
        final ItemMeta itemMeta1 = item1.getItemMeta();
        final ItemMeta itemMeta2 = item2.getItemMeta();
        if (item1.getType().equals(Material.ENCHANTED_BOOK)) {
            enchantments1 = ((EnchantmentStorageMeta) itemMeta1).getStoredEnchants();
        } else {
            enchantments1 = item1.getEnchantments();
        }
        if (item2.getType().equals(Material.ENCHANTED_BOOK)) {
            enchantments2 = ((EnchantmentStorageMeta) itemMeta2).getStoredEnchants();
        } else {
            enchantments2 = item2.getEnchantments();
        }

        // 获取待合成物品上完全相同的附魔属性
        final Map<Enchantment, Integer> enchantEquals = new HashMap<>();
        // 获取待合成物品上所有的附魔属性, 并保留等级较大的
        final Map<Enchantment, Integer> enchantBigger = new HashMap<>(enchantments2);
        enchantments1.forEach((key1, value1) -> {
            if (enchantments2.containsKey(key1) && enchantments2.get(key1).equals(value1)) {
                enchantEquals.put(key1, value1);
            }
            enchantBigger.merge(key1, value1, Integer::max);
        });

        // 获取铁砧结果栏中物品的附魔属性
        final Map<Enchantment, Integer> enchantments3;
        final ItemMeta itemMeta3 = item3.getItemMeta();
        final boolean item3IsBook;
        if (item3.getType().equals(Material.ENCHANTED_BOOK)) {
            enchantments3 = ((EnchantmentStorageMeta) itemMeta3).getStoredEnchants();
            item3IsBook = true;
        } else {
            enchantments3 = item3.getEnchantments();
            item3IsBook = false;
        }

        // enchantBigger中去除结果物品没有的附魔属性
        enchantBigger.keySet().removeIf(key -> !enchantments3.containsKey(key));

        // 创建结果附魔
        final Map<Enchantment, Integer> enchantResult = new HashMap<>();
        enchantEquals.forEach((keyEquals, valueEquals) -> {
            // 判断结果物品上的附魔属性是否小于等于待合成物品(到达原版附魔等级上限)
            if (enchantments3.get(keyEquals) <= valueEquals) {
                enchantResult.put(keyEquals, valueEquals < 9 ? ++valueEquals : 10);
            }
            enchantBigger.remove(keyEquals);
        });

        // 设置物品惩罚
        if (!enchantResult.isEmpty()) {
            ((Repairable) itemMeta3).setRepairCost((((Repairable) itemMeta3).getRepairCost() - 1) >> 1);
        }

        // 输出结果物品
        enchantResult.putAll(enchantBigger);
        if (item3IsBook) {
            for (final Map.Entry<Enchantment, Integer> entryResult : enchantResult.entrySet()) {
                ((EnchantmentStorageMeta) itemMeta3).removeStoredEnchant(entryResult.getKey());
                ((EnchantmentStorageMeta) itemMeta3).addStoredEnchant(entryResult.getKey(), entryResult.getValue(), true);
            }
        } else {
            for (final Map.Entry<Enchantment, Integer> entryResult : enchantResult.entrySet()) {
                itemMeta3.removeEnchant(entryResult.getKey());
                itemMeta3.addEnchant(entryResult.getKey(), entryResult.getValue(), true);
            }
        }
        item3.setItemMeta(itemMeta3);
        e.setResult(item3);
    }
}
