package com.kiocg.BotExtend;

import com.kiocg.BotExtend.Commands.LinkCommand;
import com.kiocg.BotExtend.Commands.LinkgetCommand;
import com.kiocg.BotExtend.GroupAdminMessage.GAMUtils;
import com.kiocg.BotExtend.GroupAdminMessage.GroupAdminMessage;
import com.kiocg.BotExtend.GroupMessage.CommandsPublic;
import com.kiocg.BotExtend.GroupMessage.GMUtils;
import com.kiocg.BotExtend.OtherEvent.OtherEvent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BotExtend extends @NotNull JavaPlugin {
    // 本类
    private static BotExtend instance;
    // Vault经济模块
    private @Nullable Economy economy;
    // Vault消息模块
    private @Nullable Chat chat;
    // 玩家绑定QQ数据的文件
    private File playersFile;
    // 玩家绑定QQ数据的配置文件
    private FileConfiguration playersFileConfiguration;

    public static @NotNull BotExtend getInstance() {
        return instance;
    }

    public @Nullable Economy getEconomy() {
        return economy;
    }

    public @Nullable Chat getChat() {
        return chat;
    }

    public @NotNull File getPlayersFile() {
        return playersFile;
    }

    public @NotNull FileConfiguration getPlayersFileConfiguration() {
        return playersFileConfiguration;
    }

    @Override
    public void onEnable() {
        instance = this;
        // 注册Vault经济模块
        try {
            economy = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Economy.class)).getProvider();
        } catch (final @NotNull NullPointerException ignore) {
            economy = null;
        }
        // 注册Vault消息模块
        try {
            chat = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Chat.class)).getProvider();
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
        pluginManager.registerEvents(new CommandsPublic(), this);
        pluginManager.registerEvents(new GroupAdminMessage(), this);
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
