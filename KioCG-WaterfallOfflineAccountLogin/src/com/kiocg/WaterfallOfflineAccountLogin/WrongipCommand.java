package com.kiocg.WaterfallOfflineAccountLogin;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class WrongipCommand extends Command {
    public WrongipCommand() {
        super("wrongip", "kiocg.command.wrongip", "wip");
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(TextComponent.fromLegacyText("用法: /wrongip <ip> 移除IP密码错误限制."));
            return;
        }

        final String ip = args[0];
        if (!Utils.wrongPasswordIPs.containsKey(ip)) {
            commandSender.sendMessage(TextComponent.fromLegacyText("此IP没有受到限制."));
            return;
        }

        Utils.wrongPasswordIPs.remove(ip);
        commandSender.sendMessage(TextComponent.fromLegacyText("成功移除IP限制: " + ip));
    }
}
