package com.kiocg.qqBot;

import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class qqBot extends JavaPlugin {
    public static qqBot instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        new KioCGBot().start();
    }

    @Override
    public void onDisable() {
        new KioCGBot().close();
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final String[] args) {
        reloadConfig();

        new KioCGBot().restart();

        sender.sendMessage("§a[§b豆渣子§a] §6成功重载插件.");
        return true;
    }
}
