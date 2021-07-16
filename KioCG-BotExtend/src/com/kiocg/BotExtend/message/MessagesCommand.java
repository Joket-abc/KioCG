package com.kiocg.BotExtend.message;

import com.kiocg.BotExtend.message.specific.At;
import com.kiocg.BotExtend.message.specific.Link;
import com.kiocg.BotExtend.message.specific.Seen;
import com.kiocg.BotExtend.message.specific.Uuid;
import com.kiocg.BotExtend.utils.AMsg;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class MessagesCommand {
    @EventHandler
    public void onCommandsPublic(final @NotNull Contact contact, final @NotNull User user, final @NotNull String userCommand) {
        final String userCommandLower = userCommand.toLowerCase();

        final String reply = AMsg.msg.get(userCommandLower);
        if (reply != null) {
            contact.sendMessage(reply);
            return;
        }

        // 公共的指令
        switch (userCommandLower) {
            // 外部信息
            case ("info"), ("介绍") -> contact.sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getMinecraftVersion() + ")\n"
                                                         + """
                                                           ①服务器里不会有任何付费内容，将来可能转型非公益后也不会有任何仅限付费内容。
                                                           ②服务器启用正版+白名单模式，最大程度减少熊孩子，并对所有恶意行为零容忍。
                                                           ③服务器不会添加任何模组、保护插件(领地锁箱子)、另类世界插件(地皮资源世界)、粘液科技。
                                                           ④服务器不会添加基岩版兼容的功能，请不要在群里询问为什么不加。""");
            case ("ip"), ("ip online"), ("地址"), ("正版地址") -> {
                try {
                    contact.sendMessage("正版限定，IP地址：play.kiocg.com"
                                        + "\n备用地址：" + InetAddress.getByName("play.kiocg.com").getHostAddress() + ":20302");
                } catch (final @NotNull UnknownHostException ignore) {
                    contact.sendMessage("正版限定，IP地址：play.kiocg.com");
                }
            }
            case ("ip offline"), ("离线地址") -> {
                try {
                    contact.sendMessage("离线账号，IP地址：offline.kiocg.com"
                                        + "\n备用地址：" + InetAddress.getByName("play.kiocg.com").getHostAddress() + ":21302");
                } catch (final @NotNull UnknownHostException ignore) {
                    contact.sendMessage("离线账号，IP地址：offline.kiocg.com");
                }
            }
            case ("status"), ("统计") -> {
                if (Bukkit.getPluginManager().isPluginEnabled("Plan")) {
                    contact.sendMessage("服务器统计信息：https://status.kiocg.com");
                } else {
                    contact.sendMessage("服务器统计信息暂时下线");
                }
            }
            case ("map"), ("地图") -> {
                final PluginManager pluginManager = Bukkit.getPluginManager();
                if (pluginManager.isPluginEnabled("dynmap") || pluginManager.isPluginEnabled("BlueMap")) {
                    contact.sendMessage("网页世界地图：https://map.kiocg.com");
                } else {
                    contact.sendMessage("网页世界地图暂时下线");
                }
            }

            // 查询信息
            case ("list"), ("在线") -> {
                final StringBuilder stringBuilder = new StringBuilder();

                Bukkit.getOnlinePlayers().forEach(player -> stringBuilder.append(player.getName()).append(", "));

                if (stringBuilder.isEmpty()) {
                    contact.sendMessage("当前没有玩家在线呢qaq");
                } else {
                    contact.sendMessage("当前在线玩家(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")："
                                        + stringBuilder.substring(0, stringBuilder.length() - 2));
                }
            }
            case ("plugin"), ("plugins"), ("插件") -> {
                final StringBuilder stringBuilder = new StringBuilder();
                for (final Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    stringBuilder.append(plugin.getName()).append(", ");
                }

                contact.sendMessage("服务端插件：" + stringBuilder.substring(0, stringBuilder.length() - 2));
            }
            case ("tps"), ("mspt"), ("状态") -> {
                final double[] tps = Bukkit.getTPS();
                contact.sendMessage("TPS(1m, 5m, 15m)："
                                    + String.format("%.2f", tps[0]) + ", "
                                    + String.format("%.2f", tps[1]) + ", "
                                    + String.format("%.2f", tps[2])
                                    + "\nMSPT(average): " + String.format("%.5f", Bukkit.getAverageTickTime()));
            }

            // 功能信息
            default -> {
                final String msg = userCommand.substring(userCommand.indexOf(' ') + 1).trim();

                if (userCommandLower.startsWith("wiki ") || userCommand.startsWith("百科 ")) {
                    contact.sendMessage("中文Minecraft Wiki镜像站(" + msg + ")：https://wiki.biligame.com/mc/" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
                    break;
                }

                if (userCommandLower.startsWith("link ") || userCommand.startsWith("连接 ")) {
                    final String playerName = PlayerLinkUtils.getPlayerLinkName(user.getId());
                    if (playerName != null) {
                        contact.sendMessage("你已经连接了游戏账号 " + playerName);
                        return;
                    }

                    new Link().link(contact, user, msg);
                    break;
                }

                if (userCommandLower.startsWith("seen ") || userCommand.startsWith("查询 ")) {
                    new Seen().seen(contact, msg);
                    break;
                }
                if (userCommandLower.startsWith("at ") || userCommand.startsWith("@ ")) {
                    new At().at(contact, user, msg);
                    break;
                }
                if (userCommandLower.startsWith("uuid ")) {
                    new Uuid().uuid(contact, msg);
                }
            }
        }
    }
}
