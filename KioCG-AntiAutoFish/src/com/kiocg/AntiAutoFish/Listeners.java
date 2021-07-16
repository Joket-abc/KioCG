package com.kiocg.AntiAutoFish;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        Utils.playerBiteTime.remove(player);
        Utils.playerAutoCount.remove(player);
    }

    @EventHandler
    public void onPlayerFish(final @NotNull PlayerFishEvent e) {
        final Player player = e.getPlayer();

        final PlayerFishEvent.State state = e.getState();
        if (state == PlayerFishEvent.State.BITE) {
            Utils.playerBiteTime.put(player, System.currentTimeMillis());
        } else if (state == PlayerFishEvent.State.CAUGHT_FISH) {
            final int count = Utils.playerAutoCount.get(player);

            if (count > 9) {
                player.kick(PaperComponents.legacySectionSerializer().deserialize("\n\n\n\n§7... §c请勿使用自动操作程序 §7...\n\n\n\n\n\n\n\n\n\n\n§8(AntiAutoFish)"));
                e.setCancelled(true);
                return;
            }

            if (Utils.playerBiteTime.get(player) - System.currentTimeMillis() < 300L) {
                Utils.playerAutoCount.put(player, count + 1);
            } else if (count > 0) {
                Utils.playerAutoCount.put(player, count - 1);
            }
        }
    }
}
