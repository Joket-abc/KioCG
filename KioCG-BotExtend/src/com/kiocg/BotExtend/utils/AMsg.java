package com.kiocg.BotExtend.utils;

import java.util.HashMap;
import java.util.Map;

public class AMsg {
    // 存储关键词、回复消息
    public static final Map<String, String> msg = new HashMap<>();

    public void setMsg() {
        //        msg.put("help", """
        //                        <> - 必要参数 [] - 可选参数
        //                        .info - 服务器介绍
        //                        .ip [offline] - 服务器IP地址
        //                        .client - 客户端下载地址
        //                        .status - 服务器统计信息
        //                        .map - 网页世界地图
        //                        .poster - 服务器宣传贴
        //                        .github - 插件项目开源
        //                        .whitelist [offline] - 白名单申请方法
        //                        .support - 赞助服务器说明
        //                        .offline - 离线(盗版)账号说明
        //                        .prefix - 自定义称号说明
        //                        .color - RGB颜色对照表
        //                        .wiki [词条] - 中文MC Wiki镜像站
        //                        .link <mcID> - 连接游戏账号
        //                        .list - 当前在线玩家
        //                        .plugin - 服务端插件
        //                        .tps - 服务端TPS
        //                        .seen <玩家> - 查询玩家
        //                        .at <玩家> [内容] - 提醒游戏内的玩家
        //                        .uuid <玩家> - 调用API查询正版UUID""");

        msg.put("qaq", "චᆽච");

        // 外部信息
        final String client = "客户端下载：http://client.kiocg.com";
        msg.put("client", client);
        msg.put("客户端", client);
        final String poster = "服务器宣传帖：https://www.mcbbs.net/thread-1173769-1-1.html";
        msg.put("poster", poster);
        msg.put("宣传帖", poster);
        final String github = "插件项目开源：https://github.com/Joket-abc/KioCG";
        msg.put("github", github);
        msg.put("开源", github);

        // 内部信息
        final String whitelist = """
                                 白名单申请(仅限正版!)：
                                 仔细阅读以下内容，申请格式失败不会通过。
                                                                                                
                                 ①请先(必须)连接一次服务器，正版IP地址：play.kiocg.com；
                                 ②然后账号持有者本人 @昵称带前缀◈的管理员 并说出你的游戏ID，
                                 ③并在最后加上"我已阅读并同意《申请白名单须知》和其他所有规定"。
                                 例如：@◈特定管理 我的ID：Player 我已阅读并同意《申请白名单须知》和其他所有规定
                                                                                                
                                 审核需要一定的时间，如果超过一小时未回复，可能管理员没看到消息，请重新申请一遍。""";
        msg.put("whitelist", whitelist);
        msg.put("白名单", whitelist);
        final String whitelistOffline = """
                                        白名单申请(仅限离线!)：
                                        仔细阅读以下内容，申请格式失败不会通过。
                                                                                                                                              
                                        ①请先前往 http://test.kiocg.com/ 参加入服资格测试，通过后截图成绩单(100分及以上)；
                                        ②再先(必须)连接一次服务器，离线IP地址：offline.kiocg.com；
                                        ③然后账号持有者本人 @昵称带前缀◈的管理员 并说出你的游戏ID，
                                        ④并在最后加上"我已阅读并同意《申请白名单须知》和其他所有规定"。
                                        ⑤同时发送成绩截图。
                                        例如：@◈特定管理 我的ID：Player 我已阅读并同意《申请白名单须知》和其他所有规定 [成绩截图]
                                                                                                                                              
                                        白名单申请通过后请使用 离线IP地址 加入游戏，并按照后续提示操作。
                                        审核需要一定的时间，如果超过一小时未回复，可能管理员没看到消息，请重新申请一遍。""";
        msg.put("whitelist offline", whitelistOffline);
        msg.put("离线白名单", whitelistOffline);
        msg.put("offline", whitelistOffline);
        msg.put("离线", whitelistOffline);
        msg.put("盗版", whitelistOffline);
        final String support = """
                               请在加入游戏后再考虑自愿为本服打赏或捐助，赞助不会有任何实质上的奖励。
                               如需退款请联系群主QQ：1105919949，退款没有有效期，只需要提供带有付款单号的截图和同平台同账号的收款二维码即可（不是二维码名片）。""";
        msg.put("support", support);
        msg.put("赞助", support);

        // 功能信息
        final String prefix = "元気达到1000即可申请4字及以内的自定义称号，查看元気请在游戏内输入/mcstats";
        msg.put("prefix", prefix);
        msg.put("称号", prefix);
        final String color = "RGB颜色对照表：https://tool.oschina.net/commons?type=3";
        msg.put("color", color);
        msg.put("rgb", color);
        msg.put("颜色", color);
        final String wiki = "中文Minecraft Wiki镜像站：https://wiki.biligame.com/mc";
        msg.put("wiki", wiki);
        msg.put("百科", wiki);

        // 连接账号
        final String link = "输入 .link <mcID> 来连接账号";
        msg.put("link", link);
        msg.put("连接", link);

        // 功能信息
        final String seen = ".seen <玩家> - 查询玩家";
        msg.put("seen", seen);
        msg.put("查询", seen);
        final String at = ".at <玩家> [内容] - 提醒游戏内的玩家";
        msg.put("at", at);
        msg.put("提醒", at);
        final String uuid = ".uuid <玩家> - 调用API查询正版UUID";
        msg.put("uuid", uuid);
    }
}
