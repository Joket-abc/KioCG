package com.kiocg.BotExtend.listeners;

import com.kiocg.BotExtend.BotExtend;
import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class InGame implements @NotNull Listener {
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!PlayerLinkUtils.hasPlayerLink(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.instance,
                                                             () -> player.sendMessage("§7[§b豆渣子§7] §6尚未连接QQ号, 请输入 /link 来查看帮助."), 5L);
        }
    }
}
