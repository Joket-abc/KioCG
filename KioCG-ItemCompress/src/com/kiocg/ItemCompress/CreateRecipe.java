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
        final List<Material> allMaterial = new ArrayList<>(Arrays.asList(Material.values()));
        // 移除空气
        allMaterial.remove(0);

        // 防止替换已有的配方
        final Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            final Object object = iterator.next();
            if (object instanceof ShapedRecipe) {
                final Map<Character, ItemStack> ingredientMap = ((ShapedRecipe) object).getIngredientMap();
                // 如果配方是由9个物品合成
                if (ingredientMap.size() == 9) {
                    final Map<ItemStack, Boolean> ingredientEquals = new HashMap<>();
                    for (final ItemStack itemStack : ingredientMap.values()) {
                        ingredientEquals.put(itemStack, true);
                    }
                    // 如果这9个物品相同
                    if (ingredientEquals.size() == 1) {
                        for (final ItemStack itemStack : ingredientEquals.keySet()) {
                            allMaterial.remove(itemStack.getType());
                        }
                    }
                }
            } else if (object instanceof ShapelessRecipe) {
                final List<ItemStack> ingredientList = ((ShapelessRecipe) object).getIngredientList();
                // 如果配方是由1个物品合成
                if (ingredientList.size() == 1) {
                    allMaterial.remove(ingredientList.get(0).getType());
                }
            }
        }

        for (final Material material : allMaterial) {
            if (material.getMaxStackSize() != 1) {
                Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(itemCompress, "ItemCompress_" + material), new ItemStack(material))
                        .addIngredient(9, material));
                Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(itemCompress, "ItemDecompress_" + material), new ItemStack(material, 9))
                        .addIngredient(1, material));
            }
        }
    }
}
