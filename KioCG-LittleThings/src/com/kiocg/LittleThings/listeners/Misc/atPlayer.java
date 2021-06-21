package com.kiocg.LittleThings.listeners.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class atPlayer implements Listener {
    // @玩家
    @EventHandler(ignoreCancelled = true)
    public void atPlayer(final @NotNull AsyncPlayerChatEvent e) {
        String message = e.getMessage();

        if (!message.contains("@") || !e.getPlayer().hasPermission("kiocg.littlethings.at")) {
            return;
        }

        // 获取在线玩家名列表，从长到短排序
        final List<String> onlinePlayersName = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> onlinePlayersName.add(player.getName()));
        onlinePlayersName.sort((a, b) -> (b.length() - a.length()));

        final String messageLowerCase = message.toLowerCase();
        for (final String playerName : onlinePlayersName) {
            final String playerNameLowerCase = playerName.toLowerCase();
            if (messageLowerCase.contains("@ " + playerNameLowerCase) || messageLowerCase.contains("@" + playerNameLowerCase)) {
                final Player thePlayer = Bukkit.getPlayer(playerName);
                Objects.requireNonNull(thePlayer).playSound(thePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);

                message = message.replaceAll("(?i)@" + playerNameLowerCase, "§9§o@§9§o" + playerName + "§r")
                                 .replaceAll("(?i)@ " + playerNameLowerCase, "§9§o@§9§o" + playerName + "§r");
            }
        }

        e.setMessage(message);
    }
}
