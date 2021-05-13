package com.kiocg.WaterfallOfflineAccountLogin;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class NewplayerCommand extends Command {
    public NewplayerCommand() {
        super("newplayer", "kiocg.command.newplayer", "np");
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(TextComponent.fromLegacyText("用法: /newplayer <ID> 生成新的离线玩家账号."));
            return;
        }

        final String playerName = args[0];
        if (!Utils.isLegalPlayerName(playerName)) {
            commandSender.sendMessage(TextComponent.fromLegacyText("非法的玩家账号. &7[0-9a-zA-Z_]{3,14}"));
            return;
        }

        final String playerAndPassword = Utils.newAccount(playerName);
        Utils.addPlayer(playerAndPassword);
        commandSender.sendMessage(TextComponent.fromLegacyText("成功创建账号: " + playerAndPassword));
    }
}
