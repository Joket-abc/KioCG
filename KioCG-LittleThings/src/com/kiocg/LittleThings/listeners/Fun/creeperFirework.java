package com.kiocg.LittleThings.listeners.Fun;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class creeperFirework implements Listener {
    // 苦力怕爆炸产生烟花
    @EventHandler(ignoreCancelled = true)
    public void creeperFirework(final @NotNull EntityExplodeEvent e) {
        final Entity entity = e.getEntity();

        if (!(entity instanceof Creeper) || new Random().nextInt(100) < 97) {
            return;
        }

        final Firework firework = entity.getWorld().spawn(e.getLocation(), Firework.class);

        final FireworkMeta fwMeta = firework.getFireworkMeta();
        fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).flicker(true).trail(true)
                                       .withColor(Color.GREEN, Color.LIME)
                                       .withFade(Color.YELLOW, Color.ORANGE).build());

        firework.setFireworkMeta(fwMeta);
    }
}
