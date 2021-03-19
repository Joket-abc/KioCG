package com.kiocg.BotExtend.GroupMessage;

import com.kiocg.BotExtend.GroupMessage.Realize.At;
import com.kiocg.BotExtend.GroupMessage.Realize.Seen;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("RedundantSuppression")
public class CommandsPrivate {
    public void onCommandsPrivate(final @NotNull GroupMessageEvent e, @NotNull String msg) {
        // 私有的指令
        switch (msg) {
            case ("qaq"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage("චᆽච");
                break;

            // 查询信息
            case ("list"):
            case ("在线"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                final StringBuilder stringBuilder = new StringBuilder();
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    stringBuilder.append(player.getName()).append(", ");
                }
                if (stringBuilder.length() == 0) {
                    e.getGroup().sendMessage("当前没有玩家在线qaq");
                } else {
                    e.getGroup().sendMessage("当前在线玩家(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")："
                                             + stringBuilder.substring(0, stringBuilder.length() - 2));
                }
                break;
            case ("plugin"):
            case ("插件"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage("服务端插件：" + Arrays.toString(Bukkit.getPluginManager().getPlugins()));
                break;
            case ("tps"):
            case ("状态"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                final double[] tps = Bukkit.getTPS();
                e.getGroup().sendMessage("TPS(1m, 5m, 15m)："
                                         + String.format("%.2f", tps[0]) + ", "
                                         + String.format("%.2f", tps[1]) + ", "
                                         + String.format("%.2f", tps[2]));
                break;

            // 功能信息
            case ("seen"):
            case ("查询"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage(".seen <玩家> - 查询玩家");
                break;
            case ("at"):
            case ("@"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage(".at <玩家> [内容] - 提醒游戏内的玩家");
                break;
            default:
                if (msg.startsWith("seen ") || msg.startsWith("查询 ")) {
                    if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                        e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                        break;
                    }

                    //noinspection DuplicateExpressions
                    msg = msg.substring(msg.indexOf(' ') + 1).trim();
                    new Seen().seen(e, msg);
                    break;
                }
                if (msg.startsWith("at ") || msg.startsWith("@ ")) {
                    if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                        e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                        break;
                    }

                    //noinspection DuplicateExpressions
                    msg = msg.substring(msg.indexOf(' ') + 1).trim();
                    new At().at(e, msg);
                    break;
                }
                // e.getGroup().sendMessage("未知指令，输入 .help 来查看帮助");
        }
    }
}
