package com.kiocg.WaterfallOfflineAccountLogin;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class WaterfallOfflineAccountLogin extends Plugin {
    public static WaterfallOfflineAccountLogin instance;

    // 玩家密码的数据文件
    public static File playersFile;
    // 玩家密码的数据配置文件
    public static Configuration playersFileConfiguration;

    @Override
    public void onEnable() {
        instance = this;

        // 加载玩家密码的数据文件
        if (!getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdir();
        }

        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            try (final InputStream inputStream = getResourceAsStream("players.yml")) {
                Files.copy(inputStream, playersFile.toPath());
                playersFile = new File(getDataFolder(), "players.yml");
            } catch (final IOException ignore) {
                BungeeCord.getInstance().getLogger().warning("加载玩家密码的数据文件失败！");
                return;
            }
        }

        // 加载玩家密码的数据配置文件
        try {
            playersFileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(playersFile);
        } catch (final IOException ignore) {
            BungeeCord.getInstance().getLogger().warning("加载玩家密码的数据配置文件失败！");
            return;
        }

        // 加载玩家密码的数据
        new Utils().loadPlayers();

        BungeeCord.getInstance().getPluginManager().registerListener(this, new Listeners());
    }

    public void savePlayersFile() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(playersFileConfiguration, playersFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
