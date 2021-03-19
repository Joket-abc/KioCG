package com.kiocg.qqBot;

import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class qqBot extends @NotNull JavaPlugin {
    @SuppressWarnings({"NonConstantFieldWithUpperCaseName"})
    public static qqBot INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

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
        sender.sendMessage("§7[§b豆渣子§7] §6重载配置文件.");
        return true;
    }
}
