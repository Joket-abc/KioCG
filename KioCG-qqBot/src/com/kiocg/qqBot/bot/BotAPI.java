package com.kiocg.qqBot.bot;

import com.kiocg.qqBot.qqBot;
import net.mamoe.mirai.Bot;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BotAPI {
    private final Bot bot;

    public BotAPI(final Bot bot) {
        this.bot = bot;
    }

    public void sendGroupMsg(final Long groupID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.getInstance(), () -> Objects.requireNonNull(bot.getGroup(groupID)).sendMessage(msg));
    }

    public void sendPrivateMsg(final Long userID, final @NotNull String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(qqBot.getInstance(), () -> Objects.requireNonNull(bot.getFriend(userID)).sendMessage(msg));
    }

    public Bot getBot() {
        return bot;
    }
}
