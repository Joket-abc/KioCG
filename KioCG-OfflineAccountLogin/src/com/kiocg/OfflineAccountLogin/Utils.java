package com.kiocg.OfflineAccountLogin;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

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
    public static @NotNull String getNewVerifyCode() {
        return RandomStringUtils.randomNumeric(6);
    }

    // 获取验证提示信息
    public static @NotNull Component getVerifyMessage(final int reason, final @NotNull String VerifyCode) {
        final Component component;

        switch (reason) {
            case 0 -> component = PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c离线账号尚未连接qq号 (请按下方提示操作) §7...\n\n\n\n\n\n\n\n\n\n\n");
            case 1, 2 -> component = PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c离线账号登录已失效 (请按下方提示操作) §7...\n\n\n\n\n\n\n\n\n\n\n");
            default -> component = PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c离线账号需要验证 (请按下方提示操作) §7...\n\n\n\n\n\n\n\n\n\n\n");
        }

        return component.append(Component.text("请给群管理员豆渣子发送临时会话消息: ", NamedTextColor.GOLD))
                        .append(Component.text(VerifyCode, NamedTextColor.WHITE));
    }
}
