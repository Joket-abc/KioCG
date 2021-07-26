package com.kiocg.BotExtend;

import com.kiocg.BotExtend.listeners.*;
import com.kiocg.BotExtend.utils.BotAdminUtils;
import com.kiocg.BotExtend.utils.NormalReplyUtils;
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

public class BotExtend extends JavaPlugin {
    public static BotExtend instance;

    // Vault经济模块
    public static @Nullable Economy economy;
    // Vault消息模块
    public static @Nullable Chat chat;

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
        // 加载机器人管理
        new BotAdminUtils().loadConfig(getConfig());

        // 加载回复关键词
        final File messagesFile = new File(getDataFolder(), "normalReply.yml");
        try {
            if (!messagesFile.exists()) {
                saveResource("normalReply.yml", false);
            }
        } catch (final @NotNull NullPointerException ignore) {
            saveResource("normalReply.yml", false);
        }
        new NormalReplyUtils().loadConfig(YamlConfiguration.loadConfiguration(messagesFile));

        // 加载玩家连接QQ的数据文件
        final File playersFile = new File(getDataFolder(), "players.yml");
        try {
            if (!playersFile.exists()) {
                saveResource("players.yml", false);
            }
        } catch (final @NotNull NullPointerException ignore) {
            saveResource("players.yml", false);
        }
        // 加载玩家连接QQ的数据配置文件
        playersFileConfiguration = YamlConfiguration.loadConfiguration(playersFile);

        // 加载玩家连接QQ的数据
        new PlayerLinkUtils().loadPlayers(playersFileConfiguration);

        // 注册事件监听器
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AdminCommand(), this);
        pluginManager.registerEvents(new UserCommand(), this);
        pluginManager.registerEvents(new GroupOther(), this);
        pluginManager.registerEvents(new InGame(), this);

        pluginManager.registerEvents(new Test(), this);
    }

    public void savePlayersFile() {
        try {
            playersFileConfiguration.save(new File(getDataFolder(), "players.yml"));
        } catch (final @NotNull IOException ex) {
            getLogger().warning("保存玩家连接QQ的数据文件失败！");
            ex.printStackTrace();
        }
    }
}
