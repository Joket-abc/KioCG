package com.kiocg.BotExtend.message;

import com.kiocg.BotExtend.message.specific.Link;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandsPublic {
    @EventHandler
    public void onCommandsPublic(final @NotNull Contact contact, final @NotNull User user, final @NotNull String userCommand) {
        // 公共的指令
        switch (userCommand.toLowerCase()) {
            case ("help") -> {
                // 仅私聊可用
                if (contact instanceof Group) {
                    break;
                }

                contact.sendMessage("""
                                    .help - 帮助
                                    .info - 服务器介绍
                                    .ip - 服务器IP地址
                                    .client - 客户端下载地址
                                    .status - 服务器统计信息
                                    .map - 网页世界地图
                                    .poster - 服务器宣传贴
                                    .github - 插件项目开源
                                    .whitelist - 白名单申请方法
                                    .support - 赞助服务器说明
                                    .offline - 离线(盗版)账号说明
                                    .prefix - 自定义称号说明
                                    .color - RGB颜色对照表
                                    .wiki - 中文MC Wiki镜像站
                                    .link <mcID> - 连接游戏账号
                                    (以下指令限定连接账号后)
                                    .list - 当前在线玩家
                                    .plugin - 服务端插件
                                    .tps - 服务端TPS
                                    .seen <玩家> - 查询玩家
                                    .at <玩家> [内容] - 提醒游戏内的玩家
                                    .uuid <玩家> - 调用API查询正版UUID""");
            }

            // 外部信息
            case ("info"), ("介绍") -> contact.sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getMinecraftVersion() + ")"
                                                         + """
                                                           ①服务器里不会有任何付费内容，将来可能转型非公益后也不会有任何仅限付费内容。
                                                           ②服务器启用正版+白名单模式，最大程度减少熊孩子，并对所有恶意行为零容忍。
                                                           ③服务器不会添加任何模组、保护插件(领地锁箱子)、另类世界插件(地皮资源世界)、粘液科技。""");
            case ("ip"), ("地址") -> {
                String spareAddress = "NULL";
                try {
                    spareAddress = InetAddress.getByName("play.kiocg.com").getHostAddress();
                } catch (final @NotNull UnknownHostException ignore) {
                }

                contact.sendMessage("正版限定，IP地址：play.kiocg.com"
                                    + "\n备用地址：" + spareAddress + ":20302");
            }
            case ("client"), ("客户端") -> contact.sendMessage("正版限定，客户端下载：http://client.kiocg.com（客户端禁止转载、修改、再分发）");
            case ("status"), ("统计") -> contact.sendMessage("服务器统计信息：https://status.kiocg.com");
            case ("map"), ("地图") -> contact.sendMessage("网页世界地图：https://map.kiocg.com");
            case ("poster"), ("宣传帖") -> contact.sendMessage("服务器宣传帖：https://www.mcbbs.net/thread-1173769-1-1.html 记得去给个好评哟~");
            case ("github"), ("开源") -> contact.sendMessage("插件项目开源：https://github.com/Joket-abc/KioCG");

            // 内部信息
            case ("whitelist"), ("白名单") -> contact.sendMessage("""
                                                               白名单申请(仅限正版!)：请先连接一次服务器，然后@任意昵称带前缀◈的管理员并报出你的ID
                                                               审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~""");
            //                final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            //                if (9 <= hour && hour <= 22) {
            //                    contact.sendMessage("""
            //                                        白名单申请(仅限正版!)：请先连接一次服务器，然后 @IbukiHoshisaki 并报出你的ID
            //                                        审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~""");
            //                } else if (23 <= hour || hour <= 1) {
            //                    contact.sendMessage("""
            //                                        白名单申请(仅限正版!)：请先连接一次服务器，然后 @StarryFK 并报出你的ID
            //                                        审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~""");
            //                } else if (hour <= 3) {
            //                    contact.sendMessage("""
            //                                        白名单申请(仅限正版!)：请先连接一次服务器，然后 @StarryFK 并报出你的ID
            //                                        审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~
            //                                        (当前时间管理员可能不在线，建议9点后再进行申请)""");
            //                } else {
            //                    contact.sendMessage("""
            //                                        白名单申请(仅限正版!)：请先连接一次服务器，然后 @IbukiHoshisaki 并报出你的ID
            //                                        审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~
            //                                        (当前时间管理员可能不在线，建议9点后再进行申请)""");
            //                }
            case ("support"), ("赞助") -> contact.sendMessage("""
                                                            请在加入游戏后再考虑自愿为本服打赏或捐助，赞助不会有任何实质上的奖励。
                                                            如需退款请联系群主QQ：1105919949，退款没有有效期，只需要提供带有付款单号的截图和同平台同账号的收款二维码即可（不是二维码名片）。""");
            case ("offline"), ("离线"), ("盗版") -> contact.sendMessage("""
                                                                    离线模式适用范围：正版玩家想要带朋友一起游玩，但是对方没有正版；
                                                                    你能明确证明自己不是熊孩子（群等级达到10以上的二次元患者）。
                                                                    如需申请离线账号请联系群主，需保证离线账号的使用者不是熊孩子。""");

            // 功能信息
            case ("prefix"), ("称号") -> contact.sendMessage("元気达到1000即可申请4字及以内的自定义称号，查看元気请在游戏内输入/mcstats");
            case ("color"), ("rgb"), ("颜色") -> contact.sendMessage("RGB颜色对照表：https://tool.oschina.net/commons?type=3");
            case ("wiki") -> contact.sendMessage("中文Minecraft Wiki的镜像站：https://wiki.biligame.com/mc/Minecraft_Wiki");

            // 功能信息
            case ("link"), ("连接") -> contact.sendMessage("输入 .link <mcID> 来连接账号");
            default -> {
                if (userCommand.toLowerCase().startsWith("link ") || userCommand.startsWith("连接 ")) {
                    final String playerName = PlayerLinkUtils.getPlayerLinkAsName(user.getId());
                    if (playerName != null) {
                        contact.sendMessage("你已经连接了游戏账号 " + playerName);
                        return;
                    }

                    new Link().link(contact, user, userCommand.substring(userCommand.indexOf(' ') + 1).trim());
                    break;
                }

                new CommandsPrivate().onCommandsPrivate(contact, user, userCommand);
            }
        }
    }
}
