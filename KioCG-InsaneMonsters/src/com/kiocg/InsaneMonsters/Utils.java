package com.kiocg.InsaneMonsters;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final List<Material> allNormalOre = new ArrayList<>() {{
        add(Material.COAL_ORE);
        add(Material.COPPER_ORE);
        add(Material.IRON_ORE);
        add(Material.GOLD_ORE);
        add(Material.REDSTONE_ORE);
        add(Material.LAPIS_ORE);
        add(Material.DIAMOND_ORE);
        add(Material.EMERALD_ORE);
    }};

    private static final List<Material> allDeepSlateOre = new ArrayList<>() {{
        add(Material.DEEPSLATE_COAL_ORE);
        add(Material.DEEPSLATE_COPPER_ORE);
        add(Material.DEEPSLATE_IRON_ORE);
        add(Material.DEEPSLATE_GOLD_ORE);
        add(Material.DEEPSLATE_REDSTONE_ORE);
        add(Material.DEEPSLATE_LAPIS_ORE);
        add(Material.DEEPSLATE_DIAMOND_ORE);
        add(Material.DEEPSLATE_EMERALD_ORE);
    }};

    public static Material getRandomNormalOre() {
        return allNormalOre.get(new Random().nextInt(allNormalOre.size()));
    }

    public static Material getRandomDeepSlateOre() {
        return allDeepSlateOre.get(new Random().nextInt(allDeepSlateOre.size()));
    }
}
