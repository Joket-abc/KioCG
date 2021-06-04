package com.kiocg.OfflineAccountLogin;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.qqBot.events.message.AsyncGroupTempMessageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        final String uuidString = player.getUniqueId().toString();
        final String ip = e.getRealAddress().getHostAddress();

        int reason = 0;

        if (PlayerLinkUtils.hasPlayerLink(uuidString)) {
            reason = 1;

            final String securityIP = Utils.playerSecurityIP.get(uuidString);
            if (securityIP != null) {
                reason = 2;

                if (!securityIP.equals(ip)) {
                    return;
                }
            }
        }

        final String verifyUUID = Utils.ipVerifyUUID.get(ip);

        if (!uuidString.equals(verifyUUID)) {
            Utils.ipVerifyUUID.put(ip, uuidString);

            final String verifyCode = Utils.getNewVerifyCode(player.getName());
            Utils.ipVerifyCode.put(ip, verifyCode);

            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Utils.getVerifyMessage(reason, verifyCode)));
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Utils.getVerifyMessage(reason, Utils.ipVerifyCode.get(ip))));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGroupTempMessage(final @NotNull AsyncGroupTempMessageEvent event) {
        final net.mamoe.mirai.event.events.GroupTempMessageEvent e = event.getEvent();

        final String msg = e.getMessage().contentToString().trim();

        Utils.ipVerifyCode.forEach((ip, verifyCode) -> {
            if (verifyCode.equals(msg)) {
                Utils.playerSecurityIP.put(Utils.ipVerifyUUID.get(ip), ip);

                Utils.ipVerifyUUID.remove(ip);
                Utils.ipVerifyCode.remove(ip);
            }
        });
    }
}
