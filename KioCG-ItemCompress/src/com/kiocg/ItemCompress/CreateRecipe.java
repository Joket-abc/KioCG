package com.kiocg.ItemCompress;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

public class CreateRecipe {
    public CreateRecipe(final ItemCompress itemCompress) {
        // 存储所有物品
        final List<Material> allMaterial = new ArrayList<>(Arrays.asList(Material.values()));
        // 存储合成配方中有且仅有单个物品的此物品、所对应的成品
        final Map<Material, ItemStack> oneItemMaterial = new EnumMap<>(Material.class);

        // 移除空气
        allMaterial.remove(0);

        // 防止覆盖已有的配方
        final Iterator<Recipe> iterator = Bukkit.recipeIterator();
        recipeWhile:
        while (iterator.hasNext()) {
            final Object recipe = iterator.next();
            if (recipe instanceof ShapelessRecipe) {
                final List<ItemStack> ingredientList = ((ShapelessRecipe) recipe).getIngredientList();
                // 如果配方是由1个物品合成
                if (ingredientList.size() == 1) {
                    oneItemMaterial.put(ingredientList.get(0).getType(), ((ShapelessRecipe) recipe).getResult());
                    Bukkit.getLogger().info("覆盖无序配方: " + ((ShapelessRecipe) recipe).getResult().getType());
                    continue;
                }

                // 如果配方是由9个物品合成
                if (ingredientList.size() == 9) {
                    final Map<ItemStack, Boolean> ingredientEquals = new HashMap<>();
                    for (final ItemStack itemStack : ingredientList) {
                        ingredientEquals.put(itemStack, true);
                    }
                    // 如果这9个物品相同
                    if (ingredientEquals.size() == 1) {
                        for (final ItemStack itemStack : ingredientEquals.keySet()) {
                            final Material material = itemStack.getType();
                            allMaterial.remove(material);
                            oneItemMaterial.remove(material);
                            Bukkit.getLogger().info("忽略无序配方: " + material);
                        }
                    }
                }
            } else if (recipe instanceof ShapedRecipe) {
                final Map<Character, ItemStack> ingredientMap = ((ShapedRecipe) recipe).getIngredientMap();
                // 如果配方是由1个物品合成
                if (ingredientMap.size() == 1) {
                    for (final ItemStack itemStack : ingredientMap.values()) {
                        oneItemMaterial.put(itemStack.getType(), ((ShapedRecipe) recipe).getResult());
                        Bukkit.getLogger().info("覆盖有序配方: " + ((ShapedRecipe) recipe).getResult().getType());
                        continue recipeWhile;
                    }
                }

                // 如果配方是由9个物品合成
                if (ingredientMap.size() == 9) {
                    final Map<ItemStack, Boolean> ingredientEquals = new HashMap<>();
                    for (final ItemStack itemStack : ingredientMap.values()) {
                        ingredientEquals.put(itemStack, true);
                    }
                    // 如果这9个物品相同
                    if (ingredientEquals.size() == 1) {
                        for (final ItemStack itemStack : ingredientEquals.keySet()) {
                            final Material material = itemStack.getType();
                            allMaterial.remove(material);
                            oneItemMaterial.remove(material);
                            Bukkit.getLogger().info("忽略有序配方: " + material);
                        }
                    }
                }
            }
        }

        for (final Material material : allMaterial) {
            if (material.isItem() && material.getMaxStackSize() != 1) {
                Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(itemCompress, "ItemCompress_" + material), new ItemStack(material))
                        .shape("aaa", "aaa", "aaa").setIngredient('a', material));
                Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(itemCompress, "ItemDecompress_" + material), new ItemStack(material, 9))
                        .addIngredient(1, material));
            }
        }
        for (final Map.Entry<Material, ItemStack> entry : oneItemMaterial.entrySet()) {
            if (entry.getKey().getMaxStackSize() != 1) {
                Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(itemCompress, "ItemDecompressCover_" + entry.getKey()), entry.getValue())
                        .addIngredient(1, entry.getKey()));
            }
        }
    }
}
