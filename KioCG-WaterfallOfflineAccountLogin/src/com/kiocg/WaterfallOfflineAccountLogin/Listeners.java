package com.kiocg.WaterfallOfflineAccountLogin;

import com.google.common.base.Charsets;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.regex.Pattern;

public class Listeners implements Listener {
    @EventHandler
    public void onPreLogin(final PreLoginEvent e) {
        final PendingConnection pendingConnection = e.getConnection();

        if (pendingConnection.isOnlineMode()) {
            e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("正版玩家请使用 play.kiocg.com 来登录服务器").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
            e.setCancelled(true);
            return;
        }

        final String playerName = pendingConnection.getName();

        if (!Pattern.matches("^[0-9a-zA-Z_]{3,16}$", playerName)) {
            e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("名称只能由3-16位字母数字下划线组成").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
            e.setCancelled(true);
            return;
        }

        final String uuidString = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8)).toString();
        //noinspection SpellCheckingInspection
        pendingConnection.setUniqueId(UUID.fromString("ffffffff-ffff-ffff" + uuidString.substring(18)));
    }
}
