package com.kiocg.InsaneMonsters;

import com.kiocg.InsaneMonsters.mobs.*;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class InsaneMonsters extends JavaPlugin {
    @SuppressWarnings("unused")
    public static InsaneMonsters instance;

    public static NamespacedKey namespacedKey;

    public static Giant giant;

    public static ZombieWarrior zombieWarrior;
    public static SkeletonArcher skeletonArcher;
    public static DarkKnight darkKnight;

    public static OreZombie oreZombie;
    public static BigSlime bigSlime;
    public static BigMagmaCube bigMagmaCube;

    @Override
    public void onEnable() {
        instance = this;

        namespacedKey = new NamespacedKey(this, "InsaneMonsters");

        giant = new Giant();

        zombieWarrior = new ZombieWarrior();
        skeletonArcher = new SkeletonArcher();
        darkKnight = new DarkKnight();

        oreZombie = new OreZombie();
        bigSlime = new BigSlime();
        bigMagmaCube = new BigMagmaCube();

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getServer().getPluginCommand("insanemonsters")).setExecutor(new IMCommand());
    }
}
