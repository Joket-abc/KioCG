package com.kiocg.InsaneMonsters;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final List<Material> allOverworldOre = new ArrayList<>() {{
        add(Material.COAL_ORE);
        add(Material.IRON_ORE);
        add(Material.GOLD_ORE);
        add(Material.REDSTONE_ORE);
        add(Material.LAPIS_ORE);
        add(Material.DIAMOND_ORE);
        add(Material.EMERALD_ORE);
    }};

    public static Material getOverworldRandomOre() {
        return allOverworldOre.get(new Random().nextInt(allOverworldOre.size()));
    }
}
