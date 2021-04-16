package com.kiocg.LittleThings.listeners;

import com.kiocg.LittleThings.utility.Utils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class Utility implements @NotNull Listener {
    // 防止重命名成内部保留的物品前缀名
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            return;
        }

        final String renameText = e.getInventory().getRenameText();

        if (Pattern.matches("^(&[0-9a-zA-Z]){3}.*$", renameText) || Pattern.matches("^(&#[0-9a-zA-Z]{6}){3}.*$", renameText)) {
            e.setResult(null);
        }
    }

    // 无法放置
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(final @NotNull BlockPlaceEvent e) {
        try {
            if (Objects.requireNonNull(e.getItemInHand().lore()).contains(LegacyComponentSerializer.legacyAmpersand().deserialize("§9无法放置"))) {
                e.setCancelled(true);
            }
        } catch (final @NotNull NullPointerException ignore) {
        }
    }

    // 防止自然刷怪塔
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        final Location location = e.getLocation();
        final EntityType entityType = e.getEntityType();

        if (Utils.isSpawnLimit(location, entityType)) {
            e.getEntity().setAI(false);
            return;
        }

        final LivingEntity entity = e.getEntity();
        final CreatureSpawnEvent.SpawnReason spawnReason = e.getSpawnReason();

        if (entity instanceof Monster && spawnReason.equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            Utils.addSpawnLimit(location, entityType);
            return;
        }

        if ((entityType.equals(EntityType.IRON_GOLEM) && spawnReason.equals(CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE))
            || (entityType.equals(EntityType.ZOMBIFIED_PIGLIN) && spawnReason.equals(CreatureSpawnEvent.SpawnReason.NETHER_PORTAL))) {
            Utils.addSpawnLimit(location, entityType);
        }
    }
}
