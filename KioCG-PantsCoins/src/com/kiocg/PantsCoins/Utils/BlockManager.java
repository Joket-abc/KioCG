package com.kiocg.PantsCoins.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockManager {
    // 存储 棕色蘑菇方块 的面属性数组、物品模型属性
    private static final Map<Boolean[], Integer> brownMushroomFaceMap = new HashMap<>();
    // 存储 红色蘑菇方块 的面属性数组、物品模型属性
    private static final Map<Boolean[], Integer> redMushroomFaceMap = new HashMap<>();
    // 存储 蘑菇柄      的面属性数组、物品模型属性
    private static final Map<Boolean[], Integer> mushroomStemFaceMap = new HashMap<>();

    public void setup() {
        final List<Integer> northIds = Arrays.asList(4, 5, 6, 7, 8, 9, 16, 17, 18, 19, 20, 21, 22, 31, 32, 33, 34, 35, 36, 37, 38, 47, 48, 49, 50, 51, 52, 53, 55, 56, 57, 58, 63, 64, 65, 66, 67, 68, 77, 78, 79, 80, 81, 82, 83, 84, 93, 94, 95, 96, 97, 98, 99, 107, 108, 109, 110, 111, 112, 113, 114, 123, 124, 125, 126, 127, 128, 129, 138, 139, 140, 141, 142, 143, 144, 145, 154, 155, 156, 157, 158, 159, 160);
        final List<Integer> eastIds = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160);
        final List<Integer> southIds = Arrays.asList(2, 3, 6, 7, 8, 9, 13, 14, 15, 19, 20, 21, 22, 27, 28, 29, 30, 35, 36, 37, 38, 43, 44, 45, 46, 51, 52, 53, 55, 56, 57, 58, 61, 62, 65, 66, 67, 68, 73, 74, 75, 76, 81, 82, 83, 84, 89, 90, 91, 92, 97, 98, 99, 103, 104, 105, 106, 111, 112, 113, 114, 119, 120, 121, 122, 127, 128, 129, 134, 135, 136, 137, 142, 143, 144, 145, 150, 151, 152, 153, 158, 159, 160);
        final List<Integer> westIds = Arrays.asList(1, 3, 5, 7, 9, 11, 12, 14, 15, 17, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 56, 58, 59, 60, 61, 62, 63, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 129, 131, 133, 135, 137, 139, 141, 143, 145, 147, 149, 151, 153, 155, 157, 159);
        final List<Integer> upIds = Arrays.asList(8, 9, 12, 15, 18, 21, 22, 25, 26, 29, 30, 33, 34, 37, 38, 41, 42, 45, 46, 49, 50, 53, 57, 58, 60, 62, 64, 67, 68, 71, 72, 75, 76, 79, 80, 83, 84, 87, 88, 91, 92, 95, 96, 99, 101, 102, 105, 106, 109, 110, 113, 114, 117, 118, 121, 122, 125, 126, 128, 129, 132, 133, 136, 137, 140, 141, 144, 145, 148, 149, 152, 153, 156, 157, 160);
        final List<Integer> downIds = Arrays.asList(23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160);

        for (int n = 1; n <= 53; ++n) {
            brownMushroomFaceMap.put(new Boolean[]{northIds.contains(n), eastIds.contains(n), southIds.contains(n), westIds.contains(n), upIds.contains(n), downIds.contains(n)},
                                     n + 1000);
        }
        for (int n = 55; n <= 99; ++n) {
            redMushroomFaceMap.put(new Boolean[]{northIds.contains(n), eastIds.contains(n), southIds.contains(n), westIds.contains(n), upIds.contains(n), downIds.contains(n)},
                                   n + 1000);
        }
        for (int n = 100; n <= 160; ++n) {
            mushroomStemFaceMap.put(new Boolean[]{northIds.contains(n), eastIds.contains(n), southIds.contains(n), westIds.contains(n), upIds.contains(n), downIds.contains(n)},
                                    n + 1000);
        }
    }

    public static void setCustomBlock(final @NotNull Block block, final int customModelData) {
        final Map<Boolean[], Integer> mushroomFaceMap;

        if (customModelData <= 1053) {
            mushroomFaceMap = brownMushroomFaceMap;
            block.setType(Material.BROWN_MUSHROOM_BLOCK);
        } else if (customModelData <= 1099) {
            mushroomFaceMap = redMushroomFaceMap;
            block.setType(Material.RED_MUSHROOM_BLOCK);
        } else {
            mushroomFaceMap = mushroomStemFaceMap;
            block.setType(Material.MUSHROOM_STEM);
        }

        for (final Map.Entry<Boolean[], Integer> entry : mushroomFaceMap.entrySet()) {
            if (entry.getValue().equals(customModelData)) {
                final Boolean[] mushroomFace = entry.getKey();
                final MultipleFacing multipleFacing = (MultipleFacing) block.getBlockData();

                multipleFacing.setFace(BlockFace.NORTH, mushroomFace[0]);
                multipleFacing.setFace(BlockFace.EAST, mushroomFace[1]);
                multipleFacing.setFace(BlockFace.SOUTH, mushroomFace[2]);
                multipleFacing.setFace(BlockFace.WEST, mushroomFace[3]);
                multipleFacing.setFace(BlockFace.UP, mushroomFace[4]);
                multipleFacing.setFace(BlockFace.DOWN, mushroomFace[5]);

                block.setBlockData(multipleFacing);
            }
        }
    }

    public static @Nullable ItemStack getCustomBlockAsItemStack(final @NotNull Block block) {
        final MultipleFacing multipleFacing = (MultipleFacing) block.getBlockData();
        final Boolean[] mushroomFace = {multipleFacing.hasFace(BlockFace.NORTH), multipleFacing.hasFace(BlockFace.EAST), multipleFacing.hasFace(BlockFace.SOUTH), multipleFacing.hasFace(BlockFace.WEST), multipleFacing.hasFace(BlockFace.UP), multipleFacing.hasFace(BlockFace.DOWN)};

        final Map<Boolean[], Integer> mushroomFaceMap;

        final Material material = block.getType();
        if (material.equals(Material.BROWN_MUSHROOM_BLOCK)) {
            mushroomFaceMap = brownMushroomFaceMap;
        } else if (material.equals(Material.RED_MUSHROOM_BLOCK)) {
            mushroomFaceMap = redMushroomFaceMap;
        } else {
            mushroomFaceMap = mushroomStemFaceMap;
        }

        for (final Map.Entry<Boolean[], Integer> entry : mushroomFaceMap.entrySet()) {
            if (Arrays.equals(entry.getKey(), mushroomFace)) {
                final ItemStack itemStack = new ItemStack(Material.BARRIER);
                final ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setCustomModelData(entry.getValue());
                itemStack.setItemMeta(itemMeta);

                return itemStack;
            }
        }
        return null;
    }
}
