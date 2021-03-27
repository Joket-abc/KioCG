package com.kiocg.BotExtend;

import com.kiocg.BotExtend.commands.Link;
import com.kiocg.BotExtend.commands.Linkget;
import com.kiocg.BotExtend.listeners.*;
import com.kiocg.BotExtend.utils.GroupAdminUtils;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BotExtend extends @NotNull JavaPlugin {
    public static BotExtend instance;

    // Vault经济模块
    public static @Nullable Economy economy;
    // Vault消息模块
    public static @Nullable Chat chat;

    // 玩家连接QQ的数据文件
    public static File playersFile;
    // 玩家连接QQ的数据配置文件
    public static FileConfiguration playersFileConfiguration;

    @Override
    public void onEnable() {
        instance = this;

        final ServicesManager servicesManager = getServer().getServicesManager();

        // 注册Vault经济模块
        try {
            economy = Objects.requireNonNull(servicesManager.getRegistration(Economy.class)).getProvider();
        } catch (final @NotNull NullPointerException ignore) {
            economy = null;
        }

        // 注册Vault消息模块
        try {
            chat = Objects.requireNonNull(servicesManager.getRegistration(Chat.class)).getProvider();
        } catch (final @NotNull NullPointerException ignore) {
            chat = null;
        }

        saveDefaultConfig();
        new GroupAdminUtils().loadConfig();

        // 加载玩家连接QQ的数据文件
        try {
            playersFile = new File(getDataFolder(), "players.yml");
            if (!playersFile.exists()) {
                saveResource("players.yml", false);
                playersFile = new File(getDataFolder(), "players.yml");
            }
        } catch (final @NotNull NullPointerException ignore) {
            saveResource("players.yml", false);
            playersFile = new File(getDataFolder(), "players.yml");
        }
        // 加载玩家连接QQ的数据配置文件
        playersFileConfiguration = YamlConfiguration.loadConfiguration(playersFile);

        // 加载玩家连接QQ的数据
        new PlayerLinkUtils().loadPlayers();

        // 注册事件监听器
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BotCommand(), this);
        pluginManager.registerEvents(new BotMessage(), this);
        pluginManager.registerEvents(new GroupOther(), this);
        pluginManager.registerEvents(new InGame(), this);

        pluginManager.registerEvents(new Test(), this);

        // 注册指令
        Objects.requireNonNull(getServer().getPluginCommand("link")).setExecutor(new Link());
        Objects.requireNonNull(getServer().getPluginCommand("linkget")).setExecutor(new Linkget());
    }

    public void savePlayersFile() {
        try {
            playersFileConfiguration.save(playersFile);
        } catch (final @NotNull IOException e) {
            e.printStackTrace();
        }
    }
}
