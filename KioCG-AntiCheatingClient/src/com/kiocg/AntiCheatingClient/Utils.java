package com.kiocg.AntiCheatingClient;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

public class Utils {
    // 获取新的验证码
    public @NotNull String getNewVerifyCode() {
        return ".say " +
                RandomStringUtils.randomAlphanumeric(180) +
                " _____________ " +
                "我已阅读并同意 \"使用作弊客户端、模组、软件等非法程序将被永久封禁\" 的条款!" +
                " _____" +
                RandomStringUtils.randomAlphanumeric(11);
    }

    // 获取验证提示信息
    public @NotNull Component getVerifyMessage(final @NotNull String verifyCode) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize("§7[§9豆渣子§7] ")
                .append(Component.text("按", NamedTextColor.GOLD))
                .append(Component.keybind("key.chat").color(NamedTextColor.GOLD))
                .append(Component.text("打开聊天框", NamedTextColor.GOLD))
                .append(Component.text(" > 戳我 < ").color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.BOLD, true).clickEvent(ClickEvent.suggestCommand(verifyCode)))
                .append(Component.text("并按回车键输入来进行安全检查.", NamedTextColor.GOLD));
    }
}
