package com.kiocg.BotExtend.message;

import com.kiocg.BotExtend.message.specific.At;
import com.kiocg.BotExtend.message.specific.Link;
import com.kiocg.BotExtend.message.specific.Seen;
import com.kiocg.BotExtend.message.specific.Uuid;
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
        // 公共的指令
        switch (userCommand.toLowerCase()) {
            /* case ("help") -> {
                contact.sendMessage("""<> - 必要参数 [] - 可选参数
                                    .info - 服务器介绍
                                    .ip [offline] - 服务器IP地址
                                    .client - 客户端下载地址
                                    .status - 服务器统计信息
                                    .map - 网页世界地图
                                    .poster - 服务器宣传贴
                                    .github - 插件项目开源
                                    .whitelist [offline] - 白名单申请方法
                                    .support - 赞助服务器说明
                                    .offline - 离线(盗版)账号说明
                                    .prefix - 自定义称号说明
                                    .color - RGB颜色对照表
                                    .wiki [词条] - 中文MC Wiki镜像站
                                    .link <mcID> - 连接游戏账号
                                    .list - 当前在线玩家
                                    .plugin - 服务端插件
                                    .tps - 服务端TPS
                                    .seen <玩家> - 查询玩家
                                    .at <玩家> [内容] - 提醒游戏内的玩家
                                    .uuid <玩家> - 调用API查询正版UUID""");
            } */

            case ("qaq") -> contact.sendMessage("චᆽච");

            // 外部信息
            case ("info"), ("介绍") -> contact.sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getBukkitVersion().split("-")[0] + ")\n"
                                                         + """
                                                           ①服务器里不会有任何付费内容，将来可能转型非公益后也不会有任何仅限付费内容。
                                                           ②服务器启用正版+白名单模式，最大程度减少熊孩子，并对所有恶意行为零容忍。
                                                           ③服务器不会添加任何模组、保护插件(领地锁箱子)、另类世界插件(地皮资源世界)、粘液科技。""");
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
            case ("client"), ("客户端") -> contact.sendMessage("客户端下载：http://client.kiocg.com");
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
            case ("poster"), ("宣传帖") -> contact.sendMessage("服务器宣传帖：https://www.mcbbs.net/thread-1173769-1-1.html");
            case ("github"), ("开源") -> contact.sendMessage("插件项目开源：https://github.com/Joket-abc/KioCG");

            // 内部信息
            case ("whitelist"), ("白名单") -> contact.sendMessage("""
                                                               白名单申请(仅限正版!)：
                                                               仔细阅读以下内容，申请格式失败不会通过。
                                                                                                                              
                                                               ①请先(必须)连接一次服务器，正版IP地址：play.kiocg.com；
                                                               ②然后账号持有者本人 @昵称带前缀◈的管理员 并说出你的游戏ID，
                                                               ③并在最后加上"我已阅读并同意《申请白名单须知》和其他所有规定"。
                                                               例如：@◈某管理 我的ID：Player 我已阅读并同意《申请白名单须知》和其他所有规定
                                                                                                                              
                                                               审核需要一定的时间，如果超过一小时未回复，可能管理员没看到消息，请重新申请一遍。""");
            case ("whitelist offline"), ("离线白名单"), ("offline"), ("离线"), ("盗版") -> contact.sendMessage("""
                                                                                                      白名单申请(仅限离线!)：
                                                                                                      仔细阅读以下内容，申请格式失败不会通过。
                                                                                                                                                                                                            
                                                                                                      ①请先前往 http://test.kiocg.com/ 参加入服资格测试，通过后截图成绩单(100分及以上)；
                                                                                                      ②再先(必须)连接一次服务器，离线IP地址：offline.kiocg.com；
                                                                                                      ③然后账号持有者本人 @昵称带前缀◈的管理员 并说出你的游戏ID，
                                                                                                      ④并在最后加上"我已阅读并同意《申请白名单须知》和其他所有规定"。
                                                                                                      ⑤同时发送成绩截图。
                                                                                                      例如：@◈Admin 我的ID：Player 我已阅读并同意《申请白名单须知》和其他所有规定 [成绩截图]
                                                                                                                                                                                                            
                                                                                                      白名单申请通过后请使用 离线IP地址 加入游戏，并按照后续提示操作。
                                                                                                      审核需要一定的时间，如果超过一小时未回复，可能管理员没看到消息，请重新申请一遍。""");
            case ("support"), ("赞助") -> contact.sendMessage("""
                                                            请在加入游戏后再考虑自愿为本服打赏或捐助，赞助不会有任何实质上的奖励。
                                                            如需退款请联系群主QQ：1105919949，退款没有有效期，只需要提供带有付款单号的截图和同平台同账号的收款二维码即可（不是二维码名片）。""");

            // 功能信息
            case ("prefix"), ("称号") -> contact.sendMessage("元気达到1000即可申请4字及以内的自定义称号，查看元気请在游戏内输入/mcstats");
            case ("color"), ("rgb"), ("颜色") -> contact.sendMessage("RGB颜色对照表：https://tool.oschina.net/commons?type=3");
            case ("wiki"), ("百科") -> contact.sendMessage("中文Minecraft Wiki镜像站：https://wiki.biligame.com/mc");

            // 连接账号
            case ("link"), ("连接") -> contact.sendMessage("输入 .link <mcID> 来连接账号");

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
                try {
                    Class.forName("com.destroystokyo.paper.event.server.ServerTickStartEvent");

                    final double[] tps = Bukkit.getTPS();
                    contact.sendMessage("TPS(1m, 5m, 15m)："
                                        + String.format("%.2f", tps[0]) + ", "
                                        + String.format("%.2f", tps[1]) + ", "
                                        + String.format("%.2f", tps[2])
                                        + "\nMSPT(average): " + String.format("%.5f", Bukkit.getAverageTickTime()));
                } catch (final ClassNotFoundException ignore) {
                    contact.sendMessage("获取TPS失败，请等待服务端升级");
                }
            }

            // 功能信息
            case ("seen"), ("查询") -> contact.sendMessage(".seen <玩家> - 查询玩家");
            case ("at"), ("@") -> contact.sendMessage(".at <玩家> [内容] - 提醒游戏内的玩家");
            case ("uuid") -> contact.sendMessage(".uuid <玩家> - 调用API查询正版UUID");

            default -> {
                final String msg = userCommand.substring(userCommand.indexOf(' ') + 1).trim();

                if (userCommand.toLowerCase().startsWith("wiki ") || userCommand.startsWith("百科 ")) {
                    contact.sendMessage("中文Minecraft Wiki镜像站(" + msg + ")：https://wiki.biligame.com/mc/" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
                    break;
                }

                if (userCommand.toLowerCase().startsWith("link ") || userCommand.startsWith("连接 ")) {
                    final String playerName = PlayerLinkUtils.getPlayerLinkName(user.getId());
                    if (playerName != null) {
                        contact.sendMessage("你已经连接了游戏账号 " + playerName);
                        return;
                    }

                    new Link().link(contact, user, msg);
                    break;
                }

                if (userCommand.toLowerCase().startsWith("seen ") || userCommand.startsWith("查询 ")) {
                    new Seen().seen(contact, msg);
                    break;
                }
                if (userCommand.toLowerCase().startsWith("at ") || userCommand.startsWith("@ ")) {
                    new At().at(contact, user, msg);
                    break;
                }
                if (userCommand.toLowerCase().startsWith("uuid ")) {
                    new Uuid().uuid(contact, msg);
                }
            }
        }
    }
}
