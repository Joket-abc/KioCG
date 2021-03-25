package com.kiocg.BotExtend;

import com.kiocg.BotExtend.Commands.LinkCommand;
import com.kiocg.BotExtend.Commands.LinkgetCommand;
import com.kiocg.BotExtend.CommandsMessage.CommandsMessage;
import com.kiocg.BotExtend.CommandsMessage.GMUtils;
import com.kiocg.BotExtend.GroupAdminMessage.AdminCommandsMessage;
import com.kiocg.BotExtend.GroupAdminMessage.AdminNotifyMessage;
import com.kiocg.BotExtend.GroupAdminMessage.GAMUtils;
import com.kiocg.BotExtend.OtherEvent.OtherEvent;
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
    // 本类
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName"})
    public static BotExtend INSTANCE;

    // Vault经济模块
    public static @Nullable Economy economy;
    // Vault消息模块
    public static @Nullable Chat chat;

    // 玩家绑定QQ数据的文件
    public static File playersFile;
    // 玩家绑定QQ数据的配置文件
    public static FileConfiguration playersFileConfiguration;

    @Override
    public void onEnable() {
        INSTANCE = this;

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
        new GAMUtils().loadConfig();

        // 加载玩家绑定数据文件
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
        playersFileConfiguration = YamlConfiguration.loadConfiguration(playersFile);

        // 加载玩家绑定数据
        new GMUtils().loadPlayers();

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new CommandsMessage(), this);
        pluginManager.registerEvents(new AdminCommandsMessage(), this);
        pluginManager.registerEvents(new AdminNotifyMessage(), this);
        pluginManager.registerEvents(new OtherEvent(), this);
        Objects.requireNonNull(getServer().getPluginCommand("link")).setExecutor(new LinkCommand());
        Objects.requireNonNull(getServer().getPluginCommand("linkget")).setExecutor(new LinkgetCommand());
    }

    public void savePlayersFile() {
        try {
            playersFileConfiguration.save(playersFile);
        } catch (final @NotNull IOException e) {
            e.printStackTrace();
        }
    }
}
