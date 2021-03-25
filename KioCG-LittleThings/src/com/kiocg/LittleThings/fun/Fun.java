package com.kiocg.LittleThings.fun;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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

public class Fun implements @NotNull Listener {
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

    // 生物特性修改
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        final LivingEntity livingEntity = e.getEntity();
        if (!(livingEntity instanceof Mob)) {
            return;
        }
        switch (e.getSpawnReason()) {
            case BREEDING:
            case BUILD_IRONGOLEM:
            case BUILD_SNOWMAN:
            case BUILD_WITHER:
            case DISPENSE_EGG:
            case EGG:
            case ENDER_PEARL:
            case JOCKEY:
            case LIGHTNING:
            case MOUNT:
            case NATURAL:
            case NETHER_PORTAL:
            case OCELOT_BABY:
            case PATROL:
            case RAID:
            case REINFORCEMENTS:
            case SPAWNER:
            case SPAWNER_EGG:
            case TRAP:
            case VILLAGE_DEFENSE:
            case VILLAGE_INVASION:
                break;
            default:
                return;
        }

        // 生物出生随机血量
        final AttributeInstance maxHealth = Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        final int randomHealth = (int) (maxHealth.getBaseValue() * (new Random().nextDouble() * 0.5 + 1.0));
        //noinspection ImplicitNumericConversion
        maxHealth.setBaseValue(randomHealth);
        //noinspection ImplicitNumericConversion
        livingEntity.setHealth(randomHealth);

        // 生物出生粒子效果
        livingEntity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, livingEntity.getLocation(), 1);
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
}
