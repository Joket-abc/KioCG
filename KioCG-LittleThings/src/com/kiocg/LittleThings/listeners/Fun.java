package com.kiocg.LittleThings.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
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

    // 鸡蛋捕捉生物(大师球)
    @EventHandler(ignoreCancelled = true)
    public void onCatchMonster(final @NotNull EntityDamageByEntityEvent e) {
        if (!e.getDamager().getType().equals(EntityType.EGG)) {
            return;
        }

        final Entity entity = e.getEntity();

        if (entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER) || !(entity instanceof Mob) || entity instanceof ElderGuardian || entity instanceof EnderDragon || entity instanceof Wither) {
            return;
        }

        final World world = entity.getWorld();
        final Location location = entity.getLocation();

        try {
            world.dropItem(location, new ItemStack(Objects.requireNonNull(Material.getMaterial(entity.getType() + "_SPAWN_EGG"))));
        } catch (final @NotNull NullPointerException ignore) {
            for (final Player player : location.getNearbyEntitiesByType(Player.class, 16.0)) {
                player.sendMessage("§a[§b豆渣子§a] §c发生内部错误, 请联系管理员!");
            }
            return;
        }

        entity.remove();

        world.createExplosion(location, 0F);
        world.playEffect(location, Effect.SMOKE, 0);
    }

    // 怪物掉落货币
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDeath(final @NotNull EntityDeathEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player || entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
            return;
        }

        final ItemStack itemStack = new ItemStack(Material.BARRIER, 1);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("铜币").decoration(TextDecoration.ITALIC, false));
        itemStack.setItemMeta(itemMeta);

        e.getDrops().add(itemStack);
    }
}
