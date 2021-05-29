package com.kiocg.WaterfallOfflineAccountLogin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class NewplayerCommand extends Command {
    public NewplayerCommand() {
        super("newplayer", "kiocg.command.newplayer", "np");
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(new ComponentBuilder("用法: /newplayer <ID> 生成新的离线玩家账号.").color(ChatColor.RED).create());
            return;
        }

        final String playerName = args[0];
        if (!Utils.isLegalPlayerName(playerName)) {
            commandSender.sendMessage(new ComponentBuilder("错误的玩家账号.").color(ChatColor.RED)
                                                                      .append(" &7[0-9a-zA-Z_]{3,14}").color(ChatColor.GRAY).create());
            return;
        }

        final String playerAndPassword = Utils.newAccount(playerName);
        Utils.addPlayer(playerAndPassword);
        commandSender.sendMessage(new ComponentBuilder("成功创建账号: ").color(ChatColor.DARK_GREEN)
                                                                  .append(playerAndPassword).color(ChatColor.GREEN)
                                                                  .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerAndPassword))
                                                                  .append(" (点击复制)").color(ChatColor.GRAY)
                                                                  .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, playerAndPassword)).create());
    }
}
