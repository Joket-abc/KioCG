package com.kiocg.LittleThings.utility;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.kiocg.LittleThings.LittleThings;
import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Utility implements @NotNull Listener {
    // 存储被白名单阻挡的玩家列表
    private final List<UUID> kickWhitelistPlayer = new ArrayList<>();

    // 广播被白名单拦截的玩家
    @EventHandler
    public void onProfileWhitelistVerify(final @NotNull ProfileWhitelistVerifyEvent e) {
        if (e.isWhitelisted() || !e.isWhitelistEnabled()) {
            return;
        }
        final PlayerProfile player = e.getPlayerProfile();
        final UUID uuid = player.getId();
        if (kickWhitelistPlayer.contains(uuid)) {
            return;
        }

        // 提醒全体玩家和群
        final String playerName = player.getName();
        for (final Player toPlayer : Bukkit.getOnlinePlayers()) {
            toPlayer.sendMessage("§7[§b豆渣子§7] §c不明生物 " + playerName + " 被白名单结界阻挡了.");
        }
        LittleThings.getInstance().getLogger().info("§c不明生物 " + playerName + " 被白名单结界阻挡了.");
        try {
            KioCGBot.getApi().sendGroupMsg(569696336L, "不明生物 " + playerName + " 被白名单结界阻挡了.");
        } catch (final @NotNull Exception ignored) {
        }
        kickWhitelistPlayer.add(uuid);
    }

    // 无法放置
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        try {
            if (Objects.requireNonNull(e.getItemInHand().getLore()).contains("§9无法放置")) {
                e.setCancelled(true);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }
}
