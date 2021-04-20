package com.kiocg.LittleThings.listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class Fun implements Listener {
    // 苦力怕爆炸产生烟花
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(final @NotNull EntityExplodeEvent e) {
        final Entity entity = e.getEntity();

        if (!(entity instanceof Creeper) || new Random().nextInt(100) < 97) {
            return;
        }

        final Firework firework = entity.getWorld().spawn(e.getLocation(), Firework.class);
        final FireworkMeta fwMeta = firework.getFireworkMeta();
        final FireworkEffect fwEffect = FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).flicker(true).trail(true)
                                                      .withColor(Color.GREEN, Color.LIME)
                                                      .withFade(Color.YELLOW, Color.ORANGE).build();

        fwMeta.addEffect(fwEffect);
        firework.setFireworkMeta(fwMeta);
    }

    // 骨粉催熟玩家
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(final @NotNull PlayerInteractEntityEvent e) {
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

    // 骷髅概率发射药水箭
    @EventHandler(ignoreCancelled = true)
    public void onEntityShootBow(final @NotNull EntityShootBowEvent e) {
        final Random random = new Random();

        if (!(e.getEntity() instanceof Skeleton) || random.nextInt(100) < 97) {
            return;
        }

        final PotionEffectType[] potionEffectTypes = PotionEffectType.values();
        ((Arrow) e.getProjectile()).addCustomEffect(new PotionEffect(potionEffectTypes[random.nextInt(potionEffectTypes.length)], 20 * 7, 0), false);
    }

    // 下届之星捕捉怪物
    @EventHandler(ignoreCancelled = true)
    public void onCatchMonster(final @NotNull PlayerInteractEntityEvent e) {
        if (!e.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        final ItemStack itemStack = e.getPlayer().getInventory().getItemInMainHand();

        if (!itemStack.getType().equals(Material.NETHER_STAR)) {
            return;
        }

        final Entity entity = e.getRightClicked();

        if (!(entity instanceof Monster) || entity instanceof WitherSkeleton || entity instanceof ElderGuardian || entity instanceof Giant || entity instanceof Wither) {
            return;
        }

        final World world = entity.getWorld();
        final Location location = entity.getLocation();

        try {
            world.dropItem(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(entity.getType() + "_SPAWN_EGG"))));
        } catch (final @NotNull NullPointerException ignore) {
            e.getPlayer().sendMessage("§a[§b豆渣子§a] §c发生内部错误, 请联系管理员!");
            return;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);
        entity.remove();

        world.createExplosion(location, 0F);
        world.playEffect(location, Effect.SMOKE, 0);
    }
}
