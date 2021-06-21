package com.kiocg.AntiCheatingClient;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储正在进行反作弊验证的玩家、验证码
    public static final Map<Player, String> playerVerifyCode = new HashMap<>();
    // 存储正在进行反作弊验证的玩家、验证提示信息缓存
    public static final Map<Player, BaseComponent[]> playerVerifyMessage = new HashMap<>();

    // 获取新的验证码
    public static @NotNull String getNewVerifyCode() {
        return ".say AntiCheatingCheck___"
               + RandomStringUtils.randomAlphanumeric(160)
               + " _______________ "
               + "我已阅读并同意 \"使用作弊客户端、模组、软件等非法程序将被永久封禁\" 等条款!"
               + " _____"
               + RandomStringUtils.randomAlphanumeric(9);
    }

    // 获取验证提示信息
    public static @NotNull BaseComponent[] getVerifyMessage(final @NotNull String verifyCode) {
        return new ComponentBuilder(new TextComponent(TextComponent.fromLegacyText("§7[§9豆渣子§7] ")))
                .append("按").color(ChatColor.GOLD)
                .append(new TranslatableComponent("key.chat")).color(ChatColor.GOLD)
                .append("打开聊天框").color(ChatColor.GOLD)
                .append(" > 戳我 < ").color(ChatColor.BOLD).color(ChatColor.YELLOW).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verifyCode))
                .append("并按回车键输入来进行安全检查.").color(ChatColor.GOLD)
                .create();
    }
}
