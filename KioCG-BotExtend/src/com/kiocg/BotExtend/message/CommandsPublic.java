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
import java.util.Calendar;

public class CommandsPublic {
    @EventHandler
    public void onCommandsPublic(final @NotNull Contact contact, final @NotNull User user, final @NotNull String userCommand) {
        // 公共的指令
        switch (userCommand.toLowerCase()) {
            case ("help"):
                // 仅私聊可用
                if (contact instanceof Group) {
                    break;
                }

                contact.sendMessage(".help - 帮助"
                                    // 外部信息
                                    + "\n.info - 服务器介绍"
                                    + "\n.ip - 服务器IP地址"
                                    + "\n.client - 客户端下载地址"
                                    + "\n.poster - 服务器宣传贴"
                                    + "\n.github - 插件项目开源"
                                    // 内部信息
                                    + "\n.whitelist - 白名单申请方法"
                                    + "\n.support - 赞助服务器说明"
                                    // 功能信息
                                    + "\n.prefix - 自定义称号说明"
                                    + "\n.color - RGB颜色对照表"
                                    + "\n.uuid - 正版UUID查询网站"
                                    + "\n.wiki - 中文MC Wiki镜像站"
                                    // 功能信息
                                    + "\n.link <mcID> - 连接游戏账号"
                                    // 私有的指令
                                    + "\n(以下指令限定连接账号后)"
                                    // 查询信息
                                    + "\n.list - 当前在线玩家"
                                    + "\n.plugin - 服务端插件"
                                    + "\n.tps - 服务端TPS"
                                    // 功能信息
                                    + "\n.seen <玩家> - 查询玩家"
                                    + "\n.at <玩家> [内容] - 提醒游戏内的玩家");
                break;

            // 外部信息
            case ("info"):
            case ("介绍"):
                contact.sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getMinecraftVersion() + ")"
                                    + "\n①服务器里不会有任何付费内容，将来可能转型非公益后也不会有任何仅限付费内容。"
                                    + "\n②服务器启用正版+白名单模式，最大程度减少熊孩子，并对所有恶意行为零容忍。"
                                    + "\n③服务器不会添加任何模组、保护插件(领地锁箱子)、另类世界插件(地皮资源世界)、粘液科技。");
                break;
            case ("ip"):
            case ("地址"):
                String spareAddress = "NULL";
                try {
                    spareAddress = InetAddress.getByName("play.kiocg.com").getHostAddress();
                } catch (final @NotNull UnknownHostException ignore) {
                }

                contact.sendMessage("正版限定，IP地址：play.kiocg.com"
                                    + "\n备用地址：" + spareAddress + ":20302");
                break;
            case ("client"):
            case ("客户端"):
                contact.sendMessage("正版限定，客户端下载：http://client.kiocg.com");
                break;
            case ("poster"):
            case ("宣传帖"):
                contact.sendMessage("服务器宣传帖：https://www.mcbbs.net/thread-1173769-1-1.html 记得去给个好评哟~");
                break;
            case ("github"):
            case ("开源"):
                contact.sendMessage("插件项目开源：https://github.com/Joket-abc/KioCG");
                break;

            // 内部信息
            case ("whitelist"):
            case ("白名单"):
                final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if (9 <= hour && hour <= 22) {
                    contact.sendMessage("白名单申请(仅限正版!)：请先连接一次服务器，然后 @IbukiHoshisaki 并报出你的ID"
                                        + "\n审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~");
                } else if (23 <= hour || hour <= 1) {
                    contact.sendMessage("白名单申请(仅限正版!)：请先连接一次服务器，然后 @StarryFK 并报出你的ID"
                                        + "\n审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~");
                } else if (hour <= 3) {
                    contact.sendMessage("白名单申请(仅限正版!)：请先连接一次服务器，然后 @StarryFK 并报出你的ID"
                                        + "\n记得查看群公告的《申请白名单须知》呐~"
                                        + "\n审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~");
                } else {
                    contact.sendMessage("白名单申请(仅限正版!)：请先连接一次服务器，然后 @IbukiHoshisaki 并报出你的ID"
                                        + "\n记得查看群公告的《申请白名单须知》呐~"
                                        + "\n审核需要一定的时间，在这之前记得查看群公告的《申请白名单须知》呐~");
                }
                break;
            case ("support"):
            case ("赞助"):
                contact.sendMessage("请在加入游戏后再考虑自愿为本服打赏或捐助，赞助不会有任何实质上的奖励。"
                                    + "\n如需退款请联系群主QQ：1105919949，退款没有有效期，只需要提供带有付款单号的截图和同平台同账号的收款二维码即可（不是二维码名片）。");
                break;

            // 功能信息
            case ("prefix"):
            case ("称号"):
                contact.sendMessage("元気达到1000即可申请4字及以内的自定义称号，查看元気请在游戏内输入/mcstats");
                break;
            case ("color"):
            case ("rgb"):
            case ("颜色"):
                contact.sendMessage("RGB颜色对照表：https://tool.oschina.net/commons?type=3");
                break;
            case ("uuid"):
                contact.sendMessage("正版UUID查询：https://mcuuid.net/");
                break;
            case ("wiki"):
                contact.sendMessage("中文Minecraft Wiki的镜像站：https://wiki.biligame.com/mc/Minecraft_Wiki");
                break;

            // 功能信息
            case ("link"):
            case ("连接"):
                contact.sendMessage("输入 .link <mcID> 来连接账号");
                break;
            default:
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
