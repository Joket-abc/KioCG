package com.kiocg.ItemCompress;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Listeners implements Listener {
    @EventHandler
    public void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent e) {
        final Recipe recipe = e.getRecipe();

        if (recipe instanceof ShapedRecipe) {
            // 压缩物品
            if (((ShapedRecipe) recipe).getKey().getKey().startsWith("itemcompress_")) {
                final CraftingInventory craftingInventory = e.getInventory();
                final ItemStack[] matrix = craftingInventory.getMatrix();

                final Set<ItemStack> ingredientEquals = new HashSet<>();

                for (final ItemStack itemStack : matrix) {
                    final ItemStack itemStackClone = itemStack.clone();
                    itemStackClone.setAmount(1);
                    ingredientEquals.add(itemStackClone);
                }

                // 如果这9个物品有不同
                if (ingredientEquals.size() != 1) {
                    craftingInventory.setResult(null);
                    return;
                }

                // 获取压缩次数
                final String multipleText;

                final ItemMeta itemMeta = matrix[0].getItemMeta();
                final Component displayNameComponent = itemMeta.displayName();
                if (displayNameComponent != null) {
                    final String displayName = PlainComponentSerializer.plain().serialize(displayNameComponent);

                    if (displayName.startsWith("§1§2§6")) {
                        multipleText = Utils.upMultiple(displayName.substring(6, 7));

                        // 超过最大压缩次数
                        if (multipleText == null) {
                            craftingInventory.setResult(null);
                            return;
                        }
                    } else {
                        multipleText = "一";
                    }
                } else {
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
                        final Component displayNameComponent = itemMeta.displayName();
                        if (displayNameComponent != null) {
                            final String displayName = PlainComponentSerializer.plain().serialize(displayNameComponent);

                            if (displayName.startsWith("§1§2§6")) {
                                multipleText = Utils.downMultiple(displayName.substring(6, 7));

                                // 解压成原版物品
                                if (multipleText == null) {
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
                        final Component displayNameComponent = itemMeta.displayName();
                        if (displayNameComponent != null) {
                            final String displayName = PlainComponentSerializer.plain().serialize(displayNameComponent);

                            if (displayName.startsWith("§1§2§6")) {
                                itemStackResult = new ItemStack(itemStack.getType(), 9);

                                multipleText = Utils.downMultiple(displayName.substring(6, 7));

                                // 解压成原版物品
                                if (multipleText == null) {
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            return;
        }

        final AnvilInventory anvilInventory = e.getInventory();

        try {
            if (Objects.requireNonNull(anvilInventory.getRenameText()).contains("压缩")
                || PlainComponentSerializer.plain().serialize(Objects.requireNonNull(Objects.requireNonNull(anvilInventory.getFirstItem()).getItemMeta().displayName())).startsWith("§1§2§6")) {
                e.setResult(null);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 防止放置压缩物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlaceEvent(final @NotNull BlockPlaceEvent e) {
        try {
            if (PlainComponentSerializer.plain().serialize(Objects.requireNonNull(e.getItemInHand().getItemMeta().displayName())).startsWith("§1§2§6")) {
                e.setCancelled(true);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    //TODO 权限判断 kiocg.itemcompress.use
}
