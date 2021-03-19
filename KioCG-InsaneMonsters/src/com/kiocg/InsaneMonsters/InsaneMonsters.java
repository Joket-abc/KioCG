package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.*;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InsaneMonsters extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName", "unused"})
    public static InsaneMonsters INSTANCE;

    public static NamespacedKey namespacedKey;

    public static Giant giant;
    public static ZombieWarrior zombieWarrior;
    public static SkeletonArcher skeletonArcher;
    public static DarkKnight darkKnight;

    public static OreZombie oreZombie;
    public static BlockZombie blockZombie;

    @Override
    public void onEnable() {
        INSTANCE = this;

        namespacedKey = new NamespacedKey(this, "InsaneMonsters");

        giant = new Giant();
        zombieWarrior = new ZombieWarrior();
        skeletonArcher = new SkeletonArcher();
        darkKnight = new DarkKnight();

        oreZombie = new OreZombie();
        blockZombie = new BlockZombie();

        getServer().getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(getServer().getPluginCommand("insanemonsters")).setExecutor(new IMCommand());
    }
}
