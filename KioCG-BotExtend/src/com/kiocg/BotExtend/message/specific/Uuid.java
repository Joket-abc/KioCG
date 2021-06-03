package com.kiocg.BotExtend.message.specific;

import com.kiocg.BotExtend.utils.HttpsUtils;
import com.kiocg.BotExtend.utils.Utils;
import net.mamoe.mirai.contact.Contact;
import org.jetbrains.annotations.NotNull;

public class Uuid {
    public void uuid(final @NotNull Contact contact, final @NotNull String msg) {
        if (!Utils.isLegalPlayerName(msg)) {
            contact.sendMessage("错误的玩家名：" + msg);
            return;
        }

        contact.sendMessage("正版玩家 " + msg + " 的UUID：" + HttpsUtils.getPlayerUUIDFromApi(msg));
    }
}
