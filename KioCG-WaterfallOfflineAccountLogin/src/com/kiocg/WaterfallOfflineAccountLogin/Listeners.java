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

import java.net.InetSocketAddress;
import java.util.UUID;

public class Listeners implements Listener {
    @EventHandler
    public void onPreLogin(final PreLoginEvent e) {
        final PendingConnection pendingConnection = e.getConnection();
        final String ip = ((InetSocketAddress) pendingConnection.getSocketAddress()).getAddress().getHostAddress();
        final int wrongTimes;

        if (!Utils.wrongPasswordIPs.containsKey(ip)) {
            wrongTimes = 0;
        } else {
            wrongTimes = Utils.wrongPasswordIPs.get(ip);

            if (wrongTimes >= 3) {
                e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("密码错误次数过多, 请联系管理员解除").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
                e.setCancelled(true);
                return;
            }
        }

        final String playerAndPassword = pendingConnection.getName();

        if (!Utils.isPasswordTrue(playerAndPassword)) {
            Utils.wrongPasswordIPs.put(ip, wrongTimes + 1);

            final int count = 3 - wrongTimes;
            e.setCancelReason(new ComponentBuilder("... ").color(ChatColor.GRAY).append("离线玩家不存在或密码错误 (" + count + ")").color(ChatColor.RED).append(" ...").color(ChatColor.GRAY).create());
            e.setCancelled(true);
            return;
        }

        final String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + Utils.splitPlayerName(playerAndPassword)).getBytes(Charsets.UTF_8)).toString();
        //noinspection SpellCheckingInspection
        pendingConnection.setUniqueId(UUID.fromString("ffffffff-ffff-ffff" + uuid.substring(18)));
    }

    @EventHandler
    public void onServerConnect(final ServerConnectEvent e) {
        final ProxiedPlayer proxiedPlayer = e.getPlayer();

        proxiedPlayer.setDisplayName(Utils.splitPlayerName(proxiedPlayer.getName()));
    }
}
