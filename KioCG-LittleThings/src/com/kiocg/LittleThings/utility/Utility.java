package com.kiocg.LittleThings.utility;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.kiocg.LittleThings.LittleThings;
import com.kiocg.qqBot.bot.KioCGBot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Utility implements Listener {
    // 存储被白名单阻挡的玩家列表
    private final List<UUID> kickWhitelistPlayer = new ArrayList<>();

    // 广播被白名单拦截的玩家
    @EventHandler
    public void onProfileWhitelistVerify(final ProfileWhitelistVerifyEvent e) {
        if (!e.isWhitelistEnabled() || e.isWhitelisted()) {
            return;
        }
        final PlayerProfile player = e.getPlayerProfile();
        final UUID uuid = player.getId();
        if (kickWhitelistPlayer.contains(uuid)) {
            return;
        }

        // 提醒全体玩家和群
        final String playerName = player.getName();
        for (final Player toPlayer : Bukkit.getServer().getOnlinePlayers()) {
            toPlayer.sendMessage("§7[§b豆渣子§7] §c不明生物 " + playerName + " 被白名单结界阻挡了.");
        }
        LittleThings.getInstance().getLogger().info("§c不明生物 " + playerName + " 被白名单结界阻挡了.");
        try {
            KioCGBot.getApi().sendGroupMsg(569696336L, "不明生物 " + playerName + " 被白名单结界阻挡了.");
        } catch (final Exception ignored) {
        }
        kickWhitelistPlayer.add(uuid);
    }

    // 鞘翅仅限末地飞行
    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlide(final EntityToggleGlideEvent e) {
        final Player player = (Player) e.getEntity();
        if (!e.isGliding()) {
            return;
        }

        if (!player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            player.sendMessage("§7[§b豆渣子§7] §c鞘翅只可以在末地飞行, 不听话可是要打屁屁的哦!");
            e.setCancelled(true);
        }
    }

    // 无法放置
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent e) {
        final ItemStack itemStack = e.getItemInHand();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && Objects.requireNonNull(itemStack.getLore()).contains("§9无法放置")) {
            e.setCancelled(true);
        }
    }
}
