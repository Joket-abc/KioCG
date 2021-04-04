package com.kiocg.ItemCompress;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Listeners implements @NotNull Listener {
    @EventHandler
    public void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent e) {
        final Recipe recipe = e.getRecipe();

        if (recipe instanceof ShapedRecipe) {
            // 压缩物品
            if (((ShapedRecipe) recipe).getKey().getKey().startsWith("itemcompress_")) {
                final CraftingInventory craftingInventory = e.getInventory();

                final Map<ItemStack, Boolean> ingredientEquals = new HashMap<>();

                for (final ItemStack itemStack : craftingInventory.getMatrix()) {
                    final ItemStack itemStackClone = itemStack.clone();
                    itemStackClone.setAmount(1);
                    ingredientEquals.put(itemStackClone, true);
                }

                // 如果这9个物品有不同
                if (ingredientEquals.size() != 1) {
                    craftingInventory.setResult(null);
                    return;
                }

                // 获取压缩次数
                String multipleText = "";

                for (final ItemStack itemStack : ingredientEquals.keySet()) {
                    final ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.hasDisplayName()) {
                        final String displayName = PlainComponentSerializer.plain().serialize(Objects.requireNonNull(itemMeta.displayName()));

                        if (displayName.startsWith("§1§2§6")) {
                            multipleText = Utils.upMultiple(displayName.substring(6, 7));

                            // 超过最大压缩次数
                            if (multipleText.isEmpty()) {
                                craftingInventory.setResult(null);
                                return;
                            }

                            break;
                        }
                    }

                    multipleText = "一";
                }

                final ItemStack itemStackResult = craftingInventory.getResult();

                final ItemMeta itemMetaResult = Objects.requireNonNull(itemStackResult).getItemMeta();
                itemMetaResult.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("§1§2§6")
                                                                    .append(Component.text(multipleText + "次压缩" + itemStackResult.getI18NDisplayName(), NamedTextColor.LIGHT_PURPLE))
                                                                    .decoration(TextDecoration.ITALIC, false));

                itemStackResult.setItemMeta(itemMetaResult);
            }
            return;
        }

        if (recipe instanceof ShapelessRecipe) {
            final String key = ((ShapelessRecipe) recipe).getKey().getKey();

            // 解压物品
            if (key.startsWith("itemdecompress_")) {
                final CraftingInventory craftingInventory = e.getInventory();

                // 获取压缩次数
                String multipleText = "";

                for (final ItemStack itemStack : craftingInventory.getMatrix()) {
                    //noinspection ConstantConditions
                    if (itemStack != null) {
                        final ItemMeta itemMeta = itemStack.getItemMeta();

                        if (itemMeta.hasDisplayName()) {
                            final String displayName = PlainComponentSerializer.plain().serialize(Objects.requireNonNull(itemMeta.displayName()));

                            if (displayName.startsWith("§1§2§6")) {
                                multipleText = Utils.downMultiple(displayName.substring(6, 7));

                                // 解压成原版物品
                                if (multipleText.isEmpty()) {
                                    return;
                                }

                                break;
                            }
                        }

                        // 物品无法被解压
                        craftingInventory.setResult(null);
                        return;
                    }
                }

                final ItemStack itemStackResult = craftingInventory.getResult();

                final ItemMeta itemMetaResult = Objects.requireNonNull(itemStackResult).getItemMeta();
                itemMetaResult.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("§1§2§6")
                                                                    .append(Component.text(multipleText + "次压缩" + itemStackResult.getI18NDisplayName(), NamedTextColor.LIGHT_PURPLE))
                                                                    .decoration(TextDecoration.ITALIC, false));

                itemStackResult.setItemMeta(itemMetaResult);
                return;
            }

            // 解压覆盖物品
            if (key.startsWith("itemdecompresscover_")) {
                final CraftingInventory craftingInventory = e.getInventory();

                ItemStack itemStackResult = null;

                // 获取压缩次数
                String multipleText = "";

                for (final ItemStack itemStack : craftingInventory.getMatrix()) {
                    //noinspection ConstantConditions
                    if (itemStack != null) {
                        final ItemMeta itemMeta = itemStack.getItemMeta();

                        if (itemMeta.hasDisplayName()) {
                            final String displayName = PlainComponentSerializer.plain().serialize(Objects.requireNonNull(itemMeta.displayName()));

                            if (displayName.startsWith("§1§2§6")) {
                                itemStackResult = new ItemStack(itemStack.getType(), 9);

                                multipleText = Utils.downMultiple(displayName.substring(6, 7));

                                // 解压成原版物品
                                if (multipleText.isEmpty()) {
                                    craftingInventory.setResult(itemStackResult);
                                    return;
                                }

                                break;
                            }
                        }

                        // 物品使用原版配方
                        return;
                    }
                }

                final ItemMeta itemMetaResult = Objects.requireNonNull(itemStackResult).getItemMeta();
                itemMetaResult.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("§1§2§6")
                                                                    .append(Component.text(multipleText + "次压缩" + itemStackResult.getI18NDisplayName(), NamedTextColor.LIGHT_PURPLE))
                                                                    .decoration(TextDecoration.ITALIC, false));

                itemStackResult.setItemMeta(itemMetaResult);

                craftingInventory.setResult(itemStackResult);
            }
        }
    }

    // 防止改名压缩物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        final AnvilInventory anvilInventory = e.getInventory();

        final ItemStack item1 = anvilInventory.getFirstItem();
        if (item1 != null) {
            try {
                if (PlainComponentSerializer.plain().serialize(Objects.requireNonNull(item1.getItemMeta().displayName())).startsWith("§1§2§6")) {
                    e.setResult(null);
                }
            } catch (final @NotNull NullPointerException ignore) {
            }
        }

        final ItemStack item2 = anvilInventory.getSecondItem();
        if (item2 != null) {
            try {
                if (PlainComponentSerializer.plain().serialize(Objects.requireNonNull(item2.getItemMeta().displayName())).startsWith("§1§2§6")) {
                    e.setResult(null);
                }
            } catch (final @NotNull NullPointerException ignore) {
            }
        }
    }

    //TODO 权限判断 kiocg.itemcompress.use
}
