package com.kiocg.OfflineAccountLogin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // 存储玩家UUID、允许登录的IP
    public static final Map<String, String> playerSecurityIP = new HashMap<>();
    // 存储登录IP、待验证玩家UUID
    public static final Map<String, String> ipVerifyUUID = new HashMap<>();
    // 存储登录IP、待验证验证码
    public static final Map<String, String> ipVerifyCode = new HashMap<>();

    // 获取新的验证码
    public static @NotNull String getNewVerifyCode(final @NotNull String PlayerName) {
        return PlayerName + "-" + RandomStringUtils.randomNumeric(6);
    }

    // 获取验证提示信息
    public static @Nullable Component getVerifyMessage(final int reason, final @NotNull String VerifyCode) {
        final Component component;

        switch (reason) {
            case 0 -> component = LegacyComponentSerializer.legacyAmpersand().deserialize("§7... §c离线账号尚未连接qq号 §7...");
            case 1 -> component = LegacyComponentSerializer.legacyAmpersand().deserialize("§7... §c离线账号尚未登录 §7...");
            case 2 -> component = LegacyComponentSerializer.legacyAmpersand().deserialize("§7... §c离线账号登录已失效 §7...");
            default -> {
                return null;
            }
        }

        return component.append(Component.text("\n \n"))
                        .append(Component.text("请给群管理员豆渣子发送以下临时会话消息", NamedTextColor.GOLD))
                        .append(Component.text(VerifyCode, NamedTextColor.YELLOW));
    }
}
