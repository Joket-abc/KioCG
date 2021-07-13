package com.kiocg.AntiCheatingClient;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储正在进行反作弊验证的玩家、验证码
    public static final Map<Player, String> playerVerifyCode = new HashMap<>();
    // 存储正在进行反作弊验证的玩家、验证提示信息缓存
    public static final Map<Player, Component> playerVerifyMessage = new HashMap<>();

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
    public static @NotNull Component getVerifyMessage(final @NotNull String verifyCode) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize("§7[§9豆渣子§7] ")
                                        .append(Component.text("按", NamedTextColor.GOLD))
                                        .append(Component.keybind("key.chat").color(NamedTextColor.GOLD))
                                        .append(Component.text("打开聊天框", NamedTextColor.GOLD))
                                        .append(Component.text(" > 戳我 < ", NamedTextColor.YELLOW, TextDecoration.BOLD)
                                                         .clickEvent(ClickEvent.suggestCommand(verifyCode)))
                                        .append(Component.text("并按回车键输入来进行安全检查.", NamedTextColor.GOLD));
    }
}
