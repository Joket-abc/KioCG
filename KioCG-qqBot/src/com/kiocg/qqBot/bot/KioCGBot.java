package com.kiocg.qqBot.bot;

import com.kiocg.qqBot.events.AsyncABEvent;
import com.kiocg.qqBot.events.message.AsyncFriendMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupMessageEvent;
import com.kiocg.qqBot.events.message.AsyncGroupTempMessageEvent;
import com.kiocg.qqBot.events.message.AsyncMessageEvent;
import com.kiocg.qqBot.qqBot;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KioCGBot {
    public static Bot bot;

    public void start() {
        // 读取主要配置文件
        final FileConfiguration config = qqBot.instance.getConfig();

        final long qq = config.getLong("bot.qq", 0L);
        final String password = config.getString("bot.password", "");

        final BotConfiguration configuration = new BotConfiguration() {
            {
                setWorkingDir(qqBot.instance.getDataFolder());
                fileBasedDeviceInfo("deviceInfo.json");

                setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("bot.protocol", "ANDROID_PAD")));

                // 是否输出额外的日志
                if (!config.getBoolean("bot.debug", false)) {
                    noNetworkLog();
                    noBotLog();
                }

                enableContactCache();
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> {
            bot = BotFactory.INSTANCE.newBot(qq, Objects.requireNonNull(password), configuration);
            bot.login();

            final PluginManager pluginManager = Bukkit.getPluginManager();
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class, event -> pluginManager.callEvent(new AsyncABEvent(event)));

            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.MessageEvent.class, event -> pluginManager.callEvent(new AsyncMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.FriendMessageEvent.class, event -> pluginManager.callEvent(new AsyncFriendMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.GroupMessageEvent.class, event -> pluginManager.callEvent(new AsyncGroupMessageEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.GroupTempMessageEvent.class, event -> pluginManager.callEvent(new AsyncGroupTempMessageEvent(event)));
        });
    }

    public void close() {
        try {
            bot.close(new Throwable());
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    public void restart() {
        close();
        start();
    }

    public static void sendGroupMsgAsync(final Long groupID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getGroup(groupID)).sendMessage(msg));
    }

    public static void sendPrivateMsgAsync(final Long userID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.instance, () -> Objects.requireNonNull(bot.getFriend(userID)).sendMessage(msg));
    }
}
