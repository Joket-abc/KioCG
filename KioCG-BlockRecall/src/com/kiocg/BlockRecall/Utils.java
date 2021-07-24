package com.kiocg.BlockRecall;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储玩家、最后放置方块的状态
    public static final Map<Player, BlockState> lastBlockState = new HashMap<>();
    // 存储玩家、最后放置方块的物品
    public static final Map<Player, ItemStack> lastBlockItemStack = new HashMap<>();
}
