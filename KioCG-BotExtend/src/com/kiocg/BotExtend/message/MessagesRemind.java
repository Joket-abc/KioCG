package com.kiocg.BotExtend.message;

import net.mamoe.mirai.contact.Group;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MessagesRemind {
    private static final @NotNull Map<Long, Long> replyTime = new HashMap<>();

    @EventHandler
    public void onMessages(final @NotNull Group group, final @NotNull String msg) {
        final Long groupID = group.getId();
        final Long time = replyTime.get(groupID);

        if (time == null) {
            replyTime.put(groupID, System.currentTimeMillis());
            return;
        }

        if (System.currentTimeMillis() - time < 1000L * 60L * 10L) {
            return;
        }

        if (msg.contains("按键精灵") || msg.contains("鼠标宏") || msg.contains("键盘宏") || msg.contains("连点") || msg.contains("自动钓鱼")) {
            group.sendMessage("你不能使用如按键精灵、鼠标宏、连点器、自动钓鱼等自动操作程序，会被视为作弊。");
        } else if (((msg.contains("刷") || msg.contains("复制")) && (msg.contains("沙子") || msg.contains("tnt") || msg.contains("地毯") || msg.contains("铁轨"))) || msg.contains("卡基岩") || msg.contains("地狱顶")) {
            group.sendMessage("你不能利用BUG刷物品、刷数据、卡基岩、卡地狱(下届)顶等，发现BUG应向群主反馈。");
        } else if (msg.contains("种子") && (msg.contains("服务器") || msg.contains("世界") || msg.contains("地图"))) {
            group.sendMessage("你不能向管理员索要物品、权限指令、世界种子，所有管理员同玩家均没有任何特权。");
        } else if ((msg.contains("村民") && !msg.contains("僵尸村民") && msg.contains("僵尸")) || (msg.contains("海龟蛋") && (msg.contains("僵尸") || msg.contains("猪人") || msg.contains("猪灵")))) {
            group.sendMessage("出于优化的需要，大部分实体的AI行为与原版有所不同，有关实体的反馈将不受支持。");
        } else if (msg.contains("死亡掉落")) {
            group.sendMessage("游戏内所有世界均不会有死亡掉落。");
        }

        replyTime.put(groupID, System.currentTimeMillis());
    }
}
