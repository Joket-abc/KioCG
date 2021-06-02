package com.kiocg.LittleThings.listeners.Misc;

import com.kiocg.LittleThings.LittleThings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class originRespawn implements Listener {
    // 死亡原地复活
    @EventHandler(priority = EventPriority.HIGHEST)
    public void originRespawn(final @NotNull PlayerRespawnEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.originrespawn.bypass")) {
            return;
        }

        final Location loc = player.getLocation();

        //TODO 1.17 Y坐标更改!
        if (loc.getY() < 0.0) {
            return;
        }

        final String worldName = loc.getWorld().getName();
        if (("KioCG_world_nether".equals(worldName) && loc.getY() > 127.0)
            || ("KioCG_world_the_end".equals(worldName) && loc.getBlock().getType() == Material.END_PORTAL)
            || "KioCG_OhTheDungeon".equals(worldName)) {
            return;
        }

        e.setRespawnLocation(loc);

        final int foodLevel = player.getFoodLevel();
        final float saturation = player.getSaturation();
        final float exhaustion = player.getExhaustion();
        Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
            player.setFoodLevel(Math.max(foodLevel, 3));
            player.setSaturation(saturation);
            player.setExhaustion(exhaustion);

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 9, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 9, 9));
        });
    }
}
