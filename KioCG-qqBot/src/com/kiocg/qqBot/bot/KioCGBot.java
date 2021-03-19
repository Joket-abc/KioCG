package com.kiocg.qqBot.bot;

import com.kiocg.qqBot.events.ABEvent;
import com.kiocg.qqBot.events.GroupMessageEvent;
import com.kiocg.qqBot.qqBot;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KioCGBot {
    private static Bot bot;
    private static BotAPI api;

    public void start() {
        // 读取主要配置文件
        final FileConfiguration config = qqBot.INSTANCE.getConfig();
        final long qq = config.getLong("main.qq", 0L);
        final String password = config.getString("main.password", "");
        final BotConfiguration configuration = new BotConfiguration() {
            {
                setWorkingDir(qqBot.INSTANCE.getDataFolder());
                fileBasedDeviceInfo("deviceInfo.json");
                setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("main.protocol", "ANDROID_PAD")));
                // 是否输出额外的日志
                if (!config.getBoolean("main.debug", false)) {
                    noNetworkLog();
                    noBotLog();
                }
                enableContactCache();
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(qqBot.INSTANCE, () -> {
            bot = BotFactory.INSTANCE.newBot(qq, Objects.requireNonNull(password), configuration);
            bot.login();
            api = new BotAPI(bot);
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class, event -> Bukkit.getPluginManager().callEvent(new ABEvent(event)));
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.events.GroupMessageEvent.class, event -> Bukkit.getPluginManager().callEvent(new GroupMessageEvent(event)));
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

    public static BotAPI getApi() {
        return api;
    }
}
