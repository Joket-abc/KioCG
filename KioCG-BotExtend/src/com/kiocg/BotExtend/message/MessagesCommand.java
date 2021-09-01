package com.kiocg.BotExtend.message;

import com.google.common.base.Joiner;
import com.kiocg.BotExtend.message.specific.Seen;
import com.kiocg.BotExtend.utils.NormalReplyUtils;
import net.mamoe.mirai.contact.Contact;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessagesCommand {
    @EventHandler
    public void onCommandsPublic(final @NotNull Contact contact, final @NotNull String userCommand) {
        final String userCommandLower = userCommand.toLowerCase();

        final String reply = NormalReplyUtils.getNormalReply(userCommandLower);
        if (reply != null) {
            contact.sendMessage(reply);
            return;
        }

        // 公共的指令
        switch (userCommandLower) {
            // 外部信息
            case ("info"), ("介绍") -> contact.sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getMinecraftVersion() + ")\n"
                                                         + """
                                                           ①服务器里不会有任何付费内容。
                                                           ②服务器启用正版+白名单模式，对所有恶意行为零容忍。
                                                           ③服务器不会添加任何模组、保护插件(领地锁箱子)、另类世界插件(地皮资源世界)、粘液科技。
                                                           ④服务器不会添加基岩版兼容的功能。""");
            case ("ip"), ("地址") -> {
                try {
                    contact.sendMessage("正版限定，IP地址：play.kiocg.com"
                                        + "\n备用地址：" + InetAddress.getByName("play.kiocg.com").getHostAddress() + ":20302");
                } catch (final @NotNull UnknownHostException ignore) {
                    contact.sendMessage("正版限定，IP地址：play.kiocg.com");
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
                final List<String> onlinePlayers = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> onlinePlayers.add(player.getName()));

                if (onlinePlayers.isEmpty()) {
                    contact.sendMessage("当前没有玩家在线呢qaq");
                } else {
                    onlinePlayers.sort(Comparator.naturalOrder());
                    contact.sendMessage("当前在线玩家(" + onlinePlayers.size() + "/" + Bukkit.getMaxPlayers() + ")："
                                        + Joiner.on(", ").join(onlinePlayers));
                }
            }
            case ("plugin"), ("plugins"), ("插件") -> {
                final List<String> plugins = new ArrayList<>();
                for (final Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    plugins.add(plugin.getName());
                }
                plugins.sort(Comparator.naturalOrder());

                contact.sendMessage("服务端插件(" + plugins.size() + ")："
                                    + Joiner.on(", ").join(plugins));
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
                final String parameter = userCommand.substring(userCommand.indexOf(' ') + 1);

                if (userCommandLower.startsWith("wiki ") || userCommand.startsWith("百科 ")) {
                    contact.sendMessage("中文Minecraft Wiki镜像站(" + parameter + ")：https://wiki.biligame.com/mc/" + URLEncoder.encode(parameter, StandardCharsets.UTF_8));
                    break;
                }

                if (userCommandLower.startsWith("seen ") || userCommand.startsWith("查询 ")) {
                    new Seen().seen(contact, parameter);
                }
            }
        }
    }
}
