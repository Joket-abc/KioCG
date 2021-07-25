package com.kiocg.qqBot;

import com.kiocg.qqBot.events.AsyncMiraiEvent;
import com.kiocg.qqBot.events.message.AsyncMiraiFriendMessageEvent;
import com.kiocg.qqBot.events.message.AsyncMiraiGroupMessageEvent;
import com.kiocg.qqBot.events.message.AsyncMiraiGroupTempMessageEvent;
import com.kiocg.qqBot.events.message.AsyncMiraiMessageEvent;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KioCGBot {
    public static Bot bot;

    public static void sendGroupMsgAsync(final Long groupID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getGroup(groupID)).sendMessage(msg));
    }

    public static void sendPrivateMsgAsync(final Long userID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getFriend(userID)).sendMessage(msg));
    }

    public static void sendGroupMsgAsync(final Long groupID, final @NotNull Message msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getGroup(groupID)).sendMessage(msg));
    }

    public static void sendPrivateMsgAsync(final Long userID, final @NotNull Message msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getFriend(userID)).sendMessage(msg));
    }

    public void start() {
        // 读取主要配置文件
        final FileConfiguration config = qqBot.instance.getConfig();

        final long qq = config.getLong("bot.qq");
        final String password = config.getString("bot.password");

        if (qq == 0L || password == null) {
            qqBot.instance.getLogger().warning("无法加载qq机器人, 请检查配置文件!");
            return;
        }

        final BotConfiguration configuration = new BotConfiguration() {
            {
                setWorkingDir(qqBot.instance.getDataFolder());
                fileBasedDeviceInfo("deviceInfo.json");

                setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("bot.protocol", "ANDROID_PAD")));

                // 是否启用列表缓存
                if (config.getBoolean("bot.cache", true)) {
                    enableContactCache();
                }

                // 是否输出额外的日志
                if (!config.getBoolean("bot.debug", false)) {
                    noNetworkLog();
                    noBotLog();
                }
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> {
            bot = BotFactory.INSTANCE.newBot(qq, password, configuration);
            bot.login();

            final PluginManager pluginManager = Bukkit.getPluginManager();

            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class, event -> pluginManager.callEvent(new AsyncMiraiEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.MessageEvent.class, event -> pluginManager.callEvent(new AsyncMiraiMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.FriendMessageEvent.class, event -> pluginManager.callEvent(new AsyncMiraiFriendMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.GroupMessageEvent.class, event -> pluginManager.callEvent(new AsyncMiraiGroupMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.GroupTempMessageEvent.class, event -> pluginManager.callEvent(new AsyncMiraiGroupTempMessageEvent(event)));
        });
    }

    public void close() {
        try {
            bot.close();
        } catch (final @NotNull NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void restart() {
        close();
        start();
    }
}
