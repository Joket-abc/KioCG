package com.kiocg.LittleThings.fun;

import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Fun implements Listener {
    // 苦力怕爆炸产生烟花
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent e) {
        final Entity entity = e.getEntity();
        if (!(entity instanceof Creeper)) {
            return;
        }
        if (new Random().nextInt(100) < 97) {
            return;
        }

        final Firework firework = entity.getWorld().spawn(e.getLocation(), Firework.class);
        final FireworkMeta fwMeta = firework.getFireworkMeta();
        final FireworkEffect fwEffect = FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).flicker(true).trail(true)
                .withColor(Color.GREEN, Color.LIME).withFade(Color.YELLOW, Color.ORANGE).build();
        fwMeta.addEffect(fwEffect);
        firework.setFireworkMeta(fwMeta);
    }

    // 骨粉催熟玩家
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        if (!e.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }
        final Entity entityClicked = e.getRightClicked();
        if (!(entityClicked instanceof Player)) {
            return;
        }
        final ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();
        if (!itemStack.getType().equals(Material.BONE_MEAL)) {
            return;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);
        ((Player) entityClicked).giveExp(1);
        final Location loc = entityClicked.getLocation();
        loc.setY(loc.getY() + 1.0);
        entityClicked.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 9, 1.0, 1.0, 1.0);
    }

    // 生物出生粒子效果
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        final Entity entity = e.getEntity();
        entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 1);
    }
}
