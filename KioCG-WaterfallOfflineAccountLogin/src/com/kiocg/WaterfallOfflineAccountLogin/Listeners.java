package com.kiocg.WaterfallOfflineAccountLogin;

import com.google.common.base.Charsets;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class Listeners implements Listener {
    @EventHandler
    public void onPreLogin(final PreLoginEvent e) {
        final PendingConnection pendingConnection = e.getConnection();
        final String playerAndPassword = pendingConnection.getName();

        if (!playerAndPassword.contains("-")) {
            e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("离线玩家不存在").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
            e.setCancelled(true);
        } else if (!Utils.isPasswordTrue(playerAndPassword)) {
            e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("离线玩家不存在").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
            e.setCancelled(true);
        }

        pendingConnection.setUniqueId(UUID.nameUUIDFromBytes(("OfflinePlayer:" + Utils.splitPlayerName(playerAndPassword)).getBytes(Charsets.UTF_8)));
    }

    @EventHandler
    public void onServerConnect(final ServerConnectEvent e) {
        final ProxiedPlayer proxiedPlayer = e.getPlayer();

        proxiedPlayer.setDisplayName(Utils.splitPlayerName(proxiedPlayer.getName()));
    }
}
