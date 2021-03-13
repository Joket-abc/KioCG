package com.kiocg.java;

import com.kiocg.java.Commands.LinkCommand;
import com.kiocg.java.Commands.LinkgetCommand;
import com.kiocg.java.GroupAdminMessage.GroupAdminMessage;
import com.kiocg.java.GroupMessage.CommandsPublic;
import com.kiocg.java.GroupMessage.GMUtils;
import com.kiocg.java.OtherEvent.OtherEvent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BotExtend extends JavaPlugin {
    private static BotExtend instance;
    private Chat chat;
    private File playersFile;
    private FileConfiguration playersFileConfiguration;

    public static BotExtend getInstance() {
        return instance;
    }

    public Chat getChat() {
        return chat;
    }

    public File getPlayersFile() {
        return playersFile;
    }

    public FileConfiguration getPlayersFileConfiguration() {
        return playersFileConfiguration;
    }

    @Override
    public void onEnable() {
        instance = this;
        // 注册Vault消息模块
        try {
            chat = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Chat.class)).getProvider();
        } catch (final NullPointerException ignore) {
            chat = null;
        }

        saveDefaultConfig();

        // 加载玩家绑定数据文件
        try {
            playersFile = new File(getDataFolder(), "players.yml");
            if (!playersFile.exists()) {
                saveResource("players.yml", false);
                playersFile = new File(getDataFolder(), "players.yml");
            }
        } catch (final NullPointerException ignore) {
            saveResource("players.yml", false);
            playersFile = new File(getDataFolder(), "players.yml");
        }
        playersFileConfiguration = YamlConfiguration.loadConfiguration(playersFile);

        // 加载玩家绑定数据
        new GMUtils().loadPlayers();

        getServer().getPluginManager().registerEvents(new CommandsPublic(), this);
        getServer().getPluginManager().registerEvents(new GroupAdminMessage(), this);
        getServer().getPluginManager().registerEvents(new OtherEvent(), this);
        Objects.requireNonNull(getServer().getPluginCommand("link")).setExecutor(new LinkCommand());
        Objects.requireNonNull(getServer().getPluginCommand("linkget")).setExecutor(new LinkgetCommand());
    }

    public void savePlayersFile() {
        try {
            playersFileConfiguration.save(playersFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
