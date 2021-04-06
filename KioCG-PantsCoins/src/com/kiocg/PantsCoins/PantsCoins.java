package com.kiocg.PantsCoins;

import com.kiocg.PantsCoins.commands.CustomModelDataCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PantsCoins extends @NotNull JavaPlugin {
    @SuppressWarnings("unused")
    public static PantsCoins instance;

    // Vault经济模块
    public static @Nullable Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;

        // 注册Vault经济模块
        try {
            economy = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Economy.class)).getProvider();
        } catch (final @NotNull NullPointerException ignore) {
            getLogger().warning("无法注册Vault经济模块!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        // TODO 出售压缩物品
        // Objects.requireNonNull(getServer().getPluginCommand("story")).setExecutor(new StoryCommand());
        Objects.requireNonNull(getServer().getPluginCommand("custommodeldata")).setExecutor(new CustomModelDataCommand());
    }
}
