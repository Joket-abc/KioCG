package com.kiocg.LittleThings.listeners;

import com.kiocg.LittleThings.utility.Utils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utility implements Listener {
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

    // 限制自然刷怪塔
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(final @NotNull CreatureSpawnEvent e) {
        if (!(e.getEntity() instanceof Monster) || !e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }

        final long blockKey = e.getLocation().toBlockKey();
        final EntityType entityType = e.getEntityType();

        if (Utils.limitMonsters.containsKey(blockKey)) {
            final List<EntityType> monstersList = Utils.limitMonsters.get(blockKey);

            if (monstersList.contains(entityType)) {
                e.setCancelled(true);
            } else {
                monstersList.add(entityType);
            }
        } else {
            Utils.limitMonsters.put(blockKey, new ArrayList<>() {{
                add(entityType);
            }});
        }
    }
}
