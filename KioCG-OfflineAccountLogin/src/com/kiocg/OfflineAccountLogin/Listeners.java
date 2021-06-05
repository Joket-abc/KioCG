package com.kiocg.OfflineAccountLogin;

import com.kiocg.BotExtend.utils.PlayerLinkUtils;
import com.kiocg.qqBot.events.message.AsyncGroupTempMessageEvent;
import net.mamoe.mirai.contact.NormalMember;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        final String uuidString = player.getUniqueId().toString();

        if (!uuidString.startsWith("ffffffff-ffff-ffff") || "ffffffff-ffff-ffff-94d3-0c5c65c18fe8".equals(uuidString)) {
            return;
        }

        final String ip = e.getRealAddress().getHostAddress();
        int reason = 0;

        if (PlayerLinkUtils.hasPlayerLink(uuidString)) {
            reason = 1;

            final String securityIP = Utils.playerSecurityIP.get(uuidString);
            if (securityIP != null) {
                reason = 2;

                if (securityIP.equals(ip)) {
                    return;
                }
            }
        }

        final String verifyUUID = Utils.ipVerifyUUID.get(ip);
        if (!uuidString.equals(verifyUUID)) {
            Utils.ipVerifyUUID.put(ip, uuidString);

            final String verifyCode = Utils.getNewVerifyCode();
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
        final NormalMember sender = e.getSender();

        for (final Map.Entry<String, String> entry : Utils.ipVerifyCode.entrySet()) {
            if (entry.getValue().equals(msg)) {
                final String uuidString = Utils.ipVerifyUUID.get(entry.getKey());

                final Long qq = PlayerLinkUtils.getPlayerLinkQQ(uuidString);
                final Long senderID = sender.getId();
                if (qq == null) {
                    PlayerLinkUtils.addPlayerLink(uuidString, senderID);
                } else if (!qq.equals(senderID)) {
                    sender.sendMessage("非连接的账号");
                    return;
                }

                Utils.playerSecurityIP.put(uuidString, entry.getKey());

                Utils.ipVerifyUUID.remove(entry.getKey());
                Utils.ipVerifyCode.remove(entry.getKey());

                sender.sendMessage("验证成功");
                return;
            }
        }

        sender.sendMessage("无效的验证码");
    }
}
