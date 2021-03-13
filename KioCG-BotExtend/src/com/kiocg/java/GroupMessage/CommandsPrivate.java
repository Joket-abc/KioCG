package com.kiocg.java.GroupMessage;

import com.kiocg.java.GroupMessage.Realize.At;
import com.kiocg.java.GroupMessage.Realize.Seen;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandsPrivate {
    public void onCommandsPrivate(final GroupMessageEvent e, String msg) {
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
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                final StringBuilder stringBuilder = new StringBuilder();
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    stringBuilder.append(player.getName()).append(", ");
                }
                e.getGroup().sendMessage("当前在线玩家(" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers() + ")：" + stringBuilder);
                break;
            case ("tps"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                final double[] tps = Bukkit.getServer().getTPS();
                e.getGroup().sendMessage("TPS(1m, 5m, 15m)：" + String.format("%.2f", tps[0]) + ", " + String.format("%.2f", tps[1]) + ", " + String.format("%.2f", tps[2]));
                break;

            // 功能信息
            case ("seen"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage(".seen <玩家> - 查询玩家");
                break;
            case ("at"):
                if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                    e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                    break;
                }

                e.getGroup().sendMessage(".at <玩家> [内容] - 提醒游戏内的玩家");
                break;
            default:
                if (msg.toLowerCase().startsWith("seen ")) {
                    if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                        e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                        break;
                    }

                    msg = msg.substring(msg.indexOf(' ') + 1);
                    new Seen().seen(e, msg);
                    break;
                }
                if (msg.toLowerCase().startsWith("at ")) {
                    if (!GMUtils.hasPlayerLink(e.getSender().getId())) {
                        e.getGroup().sendMessage("请在连接游戏账号后使用此指令\n.link <mcID> - 连接账号");
                        break;
                    }

                    msg = msg.substring(msg.indexOf(' ') + 1);
                    new At().at(e, msg);
                    break;
                }
                e.getGroup().sendMessage("未知指令，输入 .help 来查看帮助");
        }
    }
}
