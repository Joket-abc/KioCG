package com.kiocg.BotExtend.listeners;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.BotExtend.utils.Utils;
import com.kiocg.qqBot.KioCGBot;
import io.papermc.paper.text.PaperComponents;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InGame implements Listener {
    // 广播被白名单拦截的玩家
    @EventHandler
    public void onProfileWhitelistVerify(final @NotNull ProfileWhitelistVerifyEvent e) {
        if (e.isWhitelisted() || !e.isWhitelistEnabled()) {
            return;
        }

        final String playerName = e.getPlayerProfile().getName();

        if (Utils.kickWhitelistPlayer.contains(playerName)) {
            return;
        }

        Utils.kickWhitelistPlayer.add(playerName);

        final String whitelistMsg = "不明生物 " + playerName + " 被白名单结界阻挡了.";

        // 提醒全体玩家
        Bukkit.getOnlinePlayers().forEach(toPlayer -> toPlayer.sendMessage("§a[§b豆渣子§a] §c" + whitelistMsg));
        // 控制台记录
        BotExtend.instance.getLogger().info("§c" + whitelistMsg);

        // 提醒全体群成员
        try {
            KioCGBot.sendGroupMsgAsync(569696336L, whitelistMsg);
        } catch (final @NotNull RuntimeException ignored) {
        }
    }

    // 玩家不在群不允许加入服务器
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(final @NotNull AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        final Long qq = PlayerLinkUtils.getPlayerLink(e.getUniqueId().toString());

        if (qq == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c此账号未连接qq号, 请联系管理员 §7...\n\n\n\n\n\n\n\n\n\n\n§8(NotLinkQQ)"));
            return;
        }

        if (!Objects.requireNonNull(KioCGBot.bot.getGroup(Long.parseLong("569696336"))).contains(qq)
            || !Objects.requireNonNull(KioCGBot.bot.getGroup(Long.parseLong("553171328"))).contains(qq)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c请加群 569696336 后再加入服务器 §7...\n\n\n\n\n\n\n\n\n\n\n§8(NotInGroup)"));
        }
    }
}
