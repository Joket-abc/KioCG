package com.kiocg.ItemCompress;

import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("deprecation")
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
                if (itemMeta.hasDisplayName()) {
                    final String displayName = itemMeta.getDisplayName();

                    final int index = displayName.indexOf("次压缩");
                    if (index != -1) {
                        multipleText = Utils.upMultiple(displayName.substring(index - 1, index));

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
                itemMetaResult.setDisplayName("§6" + multipleText + "次压缩" + Utils.getI18NDisplayName(itemStackResult.getType().toString()));
                itemMetaResult.setCustomModelData(Utils.getCustomModelData(multipleText));
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
                            final String displayName = itemMeta.getDisplayName();

                            final int index = displayName.indexOf("次压缩");
                            if (index != -1) {
                                multipleText = Utils.downMultiple(displayName.substring(index - 1, index));

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
                itemMetaResult.setDisplayName("§6" + multipleText + "次压缩" + Utils.getI18NDisplayName(itemStackResult.getType().toString()));
                itemMetaResult.setCustomModelData(Utils.getCustomModelData(multipleText));
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
                            final String displayName = itemMeta.getDisplayName();

                            final int index = displayName.indexOf("次压缩");
                            if (index != -1) {
                                multipleText = Utils.downMultiple(displayName.substring(index - 1, index));

                                itemStackResult = new ItemStack(itemStack.getType(), 9);
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
                itemMetaResult.setDisplayName("§6" + multipleText + "次压缩" + Utils.getI18NDisplayName(itemStackResult.getType().toString()));
                itemMetaResult.setCustomModelData(Utils.getCustomModelData(multipleText));
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
                || Objects.requireNonNull(anvilInventory.getFirstItem()).getItemMeta().getDisplayName().contains("压缩")) {
                e.setResult(null);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 防止压缩物品交互
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        final Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        try {
            if (!Objects.requireNonNull(e.getItem()).getItemMeta().getDisplayName().contains("压缩")) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
            return;
        }

        try {
            if (Objects.requireNonNull(e.getClickedBlock()).getState() instanceof TileState) {
                return;
            }
        } catch (final @NotNull NullPointerException ignore) {
        }

        e.setCancelled(true);
    }

    // 防止压缩物品交互
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteractEntity(final @NotNull PlayerInteractEntityEvent e) {
        final ItemStack itemStack;
        if (e.getHand() == EquipmentSlot.HAND) {
            itemStack = e.getPlayer().getInventory().getItemInMainHand();
        } else {
            itemStack = e.getPlayer().getInventory().getItemInOffHand();
        }

        try {
            if (itemStack.getItemMeta().getDisplayName().contains("压缩")) {
                e.setCancelled(true);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    //TODO 权限判断 kiocg.itemcompress.use
}
