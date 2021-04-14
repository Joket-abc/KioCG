package com.kiocg.BotExtend.message;

import net.mamoe.mirai.contact.Group;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class MessagesRemind {
    @EventHandler
    public void onMessages(final @NotNull Group group, final @NotNull String msg) {
        if (msg.contains("按键精灵") || msg.contains("鼠标宏") || msg.contains("连点器") || msg.contains("自动钓鱼")) {
            group.sendMessage("你不能使用诸如按键精灵、鼠标宏、自动钓鱼等自动操作程序，这将会被视为作弊。");
        } else if (((msg.contains("刷") || msg.contains("复制")) && (msg.contains("沙子") || msg.contains("tnt") || msg.contains("地毯") || msg.contains("铁轨"))) || msg.contains("卡基岩") || msg.contains("地狱顶")) {
            group.sendMessage("你不能利用BUG刷物品、刷数据、卡基岩、卡地狱(下届)顶等。");
        } else if (msg.contains("种子") && (msg.contains("服务器") || msg.contains("世界") || msg.contains("地图"))) {
            group.sendMessage("你不能向管理员索要物品、指令操作、世界种子等，所有管理员同玩家均没有任何特权。");
        } else if ((msg.contains("村民") && !msg.contains("僵尸村民") && msg.contains("僵尸")) || (msg.contains("海龟蛋") && (msg.contains("僵尸") || msg.contains("猪人") || msg.contains("猪灵")))) {
            group.sendMessage("出于优化需要，所有实体的AI行为均与原版有所不同，有关实体的反馈将不受支持。");
        }
    }
}
