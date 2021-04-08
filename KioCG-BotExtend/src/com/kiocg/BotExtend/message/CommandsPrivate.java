package com.kiocg.BotExtend.message;

import com.kiocg.BotExtend.message.specific.At;
import com.kiocg.BotExtend.message.specific.Seen;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("RedundantSuppression")
public class CommandsPrivate {
    public void onCommandsPrivate(final @NotNull Contact contact, final @NotNull User user, final @NotNull String userCommand) {
        if (!PlayerLinkUtils.hasPlayerLink(user.getId())) {
            switch (userCommand.toLowerCase()) {
                case ("qaq"):

                    // 查询信息
                case ("list"):
                case ("在线"):
                case ("plugin"):
                case ("plugins"):
                case ("插件"):
                case ("tps"):
                case ("状态"):
                    contact.sendMessage("请在连接游戏账号后使用此指令\n输入 .link <mcID> 来连接账号");
                    return;
                default:
                    if (userCommand.toLowerCase().startsWith("seen") || userCommand.startsWith("查询")
                        || userCommand.toLowerCase().startsWith("at") || userCommand.charAt(0) == '@') {
                        contact.sendMessage("请在连接游戏账号后使用此指令\n输入 .link <mcID> 来连接账号");
                        return;
                    }
            }
        }

        // 私有的指令
        switch (userCommand.toLowerCase()) {
            case ("qaq"):
                contact.sendMessage("චᆽච");
                break;

            // 查询信息
            case ("list"):
            case ("在线"):
                final StringBuilder stringBuilder = new StringBuilder();

                for (final Player player : Bukkit.getOnlinePlayers()) {
                    stringBuilder.append(player.getName()).append(", ");
                }

                if (stringBuilder.length() == 0) {
                    contact.sendMessage("当前没有玩家在线呢qaq");
                } else {
                    contact.sendMessage("当前在线玩家(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")："
                                        + stringBuilder.substring(0, stringBuilder.length() - 2));
                }
                break;
            case ("plugin"):
            case ("plugins"):
            case ("插件"):
                contact.sendMessage("服务端插件：" + Arrays.toString(Bukkit.getPluginManager().getPlugins()));
                break;
            case ("tps"):
            case ("状态"):
                final double[] tps = Bukkit.getTPS();
                contact.sendMessage("TPS(1m, 5m, 15m)："
                                    + String.format("%.2f", tps[0]) + ", "
                                    + String.format("%.2f", tps[1]) + ", "
                                    + String.format("%.2f", tps[2]));
                break;

            // 功能信息
            case ("seen"):
            case ("查询"):
                contact.sendMessage(".seen <玩家> - 查询玩家");
                break;
            case ("at"):
            case ("@"):
                contact.sendMessage(".at <玩家> [内容] - 提醒游戏内的玩家");
                break;
            default:
                if (userCommand.toLowerCase().startsWith("seen ") || userCommand.startsWith("查询 ")) {
                    //noinspection DuplicateExpressions
                    new Seen().seen(contact, userCommand.substring(userCommand.indexOf(' ') + 1).trim());
                    break;
                }
                if (userCommand.toLowerCase().startsWith("at ") || userCommand.startsWith("@ ")) {
                    //noinspection DuplicateExpressions
                    new At().at(contact, user, userCommand.substring(userCommand.indexOf(' ') + 1).trim());
                    break;
                }
        }
    }
}
