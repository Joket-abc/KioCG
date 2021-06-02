package com.kiocg.LittleThings.listeners.Fun;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class skeletonPotion implements Listener {
    // 骷髅概率发射药水箭
    @EventHandler(ignoreCancelled = true)
    public void skeletonPotion(final @NotNull EntityShootBowEvent e) {
        final Random random = new Random();

        if (!(e.getEntity() instanceof Skeleton) || random.nextInt(100) < 97) {
            return;
        }

        final PotionEffectType[] potionEffectTypes = PotionEffectType.values();
        ((Arrow) e.getProjectile()).addCustomEffect(new PotionEffect(potionEffectTypes[random.nextInt(potionEffectTypes.length)], 20 * 7, random.nextInt(3)), false);
    }
}
