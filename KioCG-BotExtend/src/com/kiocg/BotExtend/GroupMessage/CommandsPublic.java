package com.kiocg.BotExtend.GroupMessage;

import com.kiocg.BotExtend.GroupMessage.Realize.Link;
import com.kiocg.qqBot.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandsPublic implements Listener {
    @EventHandler
    public void onCommandsPublic(final GroupMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupMessageEvent e = event.getEvent();

        String msg = e.getMessage().contentToString().trim();
        try {
            if ((msg.charAt(0) != '.' && msg.charAt(0) != '。') || msg.charAt(1) == '.' || msg.charAt(1) == '。') {
                return;
            }
        } catch (final IndexOutOfBoundsException ignore) {
            return;
        }
        msg = msg.substring(1);

        // 公共的指令
        switch (msg.toLowerCase()) {
            // 取消了.help指令
            case ("HELP"):
                e.getGroup().sendMessage(".help - 帮助"
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
                        // 功能信息
                        + "\n.link <mcID> - 连接账号"
                        // 私有的指令
                        + "\n(以下指令限定连接账号后)"
                        // 查询信息
                        + "\n.list - 当前在线玩家"
                        + "\n.tps - 服务器TPS"
                        // 功能信息
                        + "\n.seen <玩家> - 查询玩家"
                        + "\n.at <玩家> [内容] - 提醒游戏内的玩家");
                break;

            // 外部信息
            case ("info"):
                e.getGroup().sendMessage("[公益、正版、原版] (当前版本" + Bukkit.getMinecraftVersion() + ")"
                        + "\n①服务器里不会有任何付费内容，将来可能转型非公益后也不会有任何仅限付费内容。"
                        + "\n②服务器启用正版+白名单模式，最大程度减少熊孩子，并对所有恶意行为零容忍。"
                        + "\n③服务器不会添加任何模组、保护插件(领地锁箱子)、经济插件、多世界插件(地皮资源世界)。");
                break;
            case ("ip"):
                String spareAddress = "NULL";
                try {
                    spareAddress = InetAddress.getByName("play.kiocg.com").getHostAddress();
                } catch (final UnknownHostException ignore) {
                }
                e.getGroup().sendMessage("正版限定，IP地址：play.kiocg.com"
                        + "\n备用地址：" + spareAddress + ":20205");
                break;
            case ("client"):
                e.getGroup().sendMessage("正版限定，客户端下载：群文件或百度网盘https://pan.baidu.com/s/1lq0o1Ma2ox2maLA2B3DqpQ");
                break;
            case ("poster"):
                e.getGroup().sendMessage("服务器宣传帖：https://www.mcbbs.net/thread-1173769-1-1.html 记得去给个好评哟~");
                break;
            case ("github"):
                e.getGroup().sendMessage("插件项目开源：https://github.com/Joket-abc/KioCG");
                break;

            // 内部信息
            case ("whitelist"):
                e.getGroup().sendMessage("白名单申请(仅限正版!)：请先进入一次服务器，然后带上你的ID @IbukiHoshisaki或者@saplingX"
                        + "\n非正版号不能申请！非正版号不能申请！非正版号不能申请！");
                break;
            case ("support"):
                e.getGroup().sendMessage("请在加入游戏后再考虑自愿为本服打赏或捐助，赞助不会有任何实质上的奖励。"
                        + "\n如需退款请联系群主QQ：1105919949，退款没有有效期，只需要提供带有付款单号的截图和同平台同账号的收款二维码即可（不是二维码名片）。");
                break;

            // 功能信息
            case ("prefix"):
                e.getGroup().sendMessage("元気值达到1000即可申请4字及以内的自定义称号，查看元気值请在游戏内输入/mcstats");
                break;
            case ("color"):
            case ("rgb"):
                e.getGroup().sendMessage("RGB颜色对照表：https://tool.oschina.net/commons?type=3");
                break;
            case ("uuid"):
                e.getGroup().sendMessage("正版UUID查询：https://mcuuid.net/");
                break;

            // 功能信息
            case ("link"):
                e.getGroup().sendMessage(".link <mcID> - 连接账号");
                break;
            default:
                if (msg.startsWith("link ")) {
                    final String playerName = GMUtils.getPlayerLinkAsName(e.getSender().getId());
                    if (playerName != null) {
                        e.getGroup().sendMessage("你已经连接了游戏账号 " + playerName);
                        return;
                    }

                    msg = msg.substring(msg.indexOf(' ') + 1);
                    new Link().link(e, msg);
                    break;
                }
                new CommandsPrivate().onCommandsPrivate(e, msg);
        }
    }
}
