package com.kiocg.ItemCompress;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.*;

public class CreateRecipe {
    public CreateRecipe() {
        // 存储能够压缩与解压的物品
        final Set<Material> compressMaterial = new HashSet<>(Arrays.asList(Material.values()));
        // 存储合成配方中仅有单个物品的配方物品匹配表、所对应的成品
        // 配方物品匹配表 例如: 所有颜色的羊毛为羊毛类配方物品匹配表
        final Map<List<RecipeChoice>, ItemStack> oneItemMaterial = new HashMap<>();

        // 胖次币功能需要
        compressMaterial.remove(Material.BARRIER);
        // 修复某些小问题
        compressMaterial.remove(Material.PLAYER_HEAD);
        compressMaterial.remove(Material.SPAWNER);

        // 防止覆盖已有的配方
        final Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            final Recipe recipe = iterator.next();

            if (recipe instanceof ShapelessRecipe) {
                final List<ItemStack> ingredientList = ((ShapelessRecipe) recipe).getIngredientList();

                // 如果配方是由1个物品合成
                if (ingredientList.size() == 1) {
                    final ItemStack result = recipe.getResult();
                    oneItemMaterial.put(((ShapelessRecipe) recipe).getChoiceList(), result);

                    // ItemCompress.instance.getLogger().info("覆盖无序配方: " + ingredientList.get(0).getType() + " -> " + result.getType() + " x" + result.getAmount());
                    continue;
                }

                // 如果配方是由9个物品合成
                if (ingredientList.size() == 9) {
                    final Set<ItemStack> ingredientEquals = new HashSet<>(ingredientList);

                    // 如果这9个物品相同
                    if (ingredientEquals.size() == 1) {
                        ingredientEquals.forEach(ingredient -> {
                            final Material material = ingredient.getType();
                            compressMaterial.remove(material);

                            // ItemCompress.instance.getLogger().info("忽略无序配方: " + material + " x9 -> " + recipe.getResult().getType() + " x" + recipe.getResult().getAmount());
                        });
                    }
                }
                continue;
            }

            if (recipe instanceof ShapedRecipe) {
                final Map<Character, ItemStack> ingredientMap = ((ShapedRecipe) recipe).getIngredientMap();

                // 如果配方是由9个物品合成
                if (ingredientMap.size() == 9) {
                    final Set<ItemStack> ingredientEquals = new HashSet<>(ingredientMap.values());

                    // 如果这9个物品相同
                    if (ingredientEquals.size() == 1) {
                        ingredientEquals.forEach(ingredient -> {
                            final Material material = ingredient.getType();
                            compressMaterial.remove(material);

                            // ItemCompress.instance.getLogger().info("忽略有序配方: " + material + " x9 -> " + recipe.getResult().getType() + " x" + recipe.getResult().getAmount());
                        });
                    }
                }
            }
        }

        // 创建压缩与解压的物品配方
        compressMaterial.forEach(material -> {
            if (material.isItem() && !material.isAir() && material.getMaxStackSize() != 1) {
                Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(ItemCompress.instance, "ItemCompress_" + material), new ItemStack(material))
                                         .shape("aaa", "aaa", "aaa").setIngredient('a', material));

                Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(ItemCompress.instance, "ItemDecompress_" + material), new ItemStack(material, 9))
                                         .addIngredient(1, material));
            }
        });
        // 创建覆盖解压的物品配方
        oneItemMaterial.forEach((recipeChoiceList, result) -> {
            final String namespacedKeyKey = "ItemDecompressCover_" + ((RecipeChoice.MaterialChoice) recipeChoiceList.get(0)).getItemStack().getType();

            final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(ItemCompress.instance, namespacedKeyKey), result);
            recipeChoiceList.forEach(shapelessRecipe::addIngredient);

            Bukkit.addRecipe(shapelessRecipe);
        });
    }
}
