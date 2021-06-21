package com.kiocg.AntiCheatingClient;

import com.kiocg.qqBot.bot.KioCGBot;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void startPlayerVerify(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.anticheatingclient.bypass")) {
            return;
        }

        final String verifyCode = Utils.getNewVerifyCode();
        final BaseComponent[] verifyMessage = Utils.getVerifyMessage(verifyCode);

        Utils.playerVerifyCode.put(player, verifyCode);
        Utils.playerVerifyMessage.put(player, verifyMessage);

        // 延迟10tick防止信息被忽视
        Bukkit.getScheduler().runTaskLaterAsynchronously(AntiCheatingClient.instance, () -> player.spigot().sendMessage(verifyMessage), 10L);
    }

    @EventHandler
    public void removePlayerVerify(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        Utils.playerVerifyCode.remove(player);
        Utils.playerVerifyMessage.remove(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerVerify(final @NotNull AsyncPlayerChatEvent e) {
        final String message = e.getMessage();

        // 可能未使用作弊端
        if (message.startsWith(".say AntiCheatingCheck___")) {
            // 消息为反作弊前缀即取消, 无论是否在进行反作弊验证
            e.setCancelled(true);

            final Player player = e.getPlayer();
            final String playerVerifyCode = Utils.playerVerifyCode.get(player);

            if (playerVerifyCode == null) {
                return;
            }

            if (message.equals(playerVerifyCode)) {
                player.sendMessage("§a[§b豆渣子§a] §2你好像很健康呐, 可以开始异世界的探险了~");

                Utils.playerVerifyCode.remove(player);
                Utils.playerVerifyMessage.remove(player);
            } else {
                player.sendMessage("§a[§b豆渣子§a] §6请不要修改文本内容呢.");
            }

            return;
        }

        // 可能使用作弊端
        if (message.startsWith("AntiCheatingCheck___")) {
            // 消息为反作弊前缀即取消, 无论是否在进行反作弊验证
            e.setCancelled(true);

            final Player player = e.getPlayer();

            if (!Utils.playerVerifyCode.containsKey(player)) {
                return;
            }

            if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) > 20 * 60 * 60 * 12) {
                // 临时封禁非萌新玩家24小时
                final Date date = new Date();
                date.setTime(date.getTime() + 1000L * 60L * 60L * 24L);
                Bukkit.getScheduler().runTask(AntiCheatingClient.instance, () -> {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(String.valueOf(player.getUniqueId()), "\n\n\n\n§7... §c快关掉快关掉 作弊可不是好孩子 §7...", date, null);
                    player.kickPlayer("\n\n\n\n§7... §c快关掉快关掉 作弊可不是好孩子 §7...");
                });
            } else {
                // 永久封禁萌新玩家
                Bukkit.getScheduler().runTask(AntiCheatingClient.instance, () -> {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(String.valueOf(player.getUniqueId()), "\n\n\n\n§7... §c尝试作弊而被永久封禁 §7...", null, null);
                    player.kickPlayer("\n\n\n\n§7... §c尝试作弊而被永久封禁 §7...");
                });
            }

            final String banMsg = "邪恶生物 " + player.getName() + " 被安全检查拦截了.";

            // 提醒全体玩家
            Bukkit.getOnlinePlayers().forEach(toPlayer -> toPlayer.sendMessage("§a[§b豆渣子§a] §c" + banMsg));
            // 控制台记录
            AntiCheatingClient.instance.getLogger().info("§c" + banMsg);

            // 提醒全体群成员
            try {
                KioCGBot.sendGroupMsgAsync(569696336L, banMsg);
            } catch (final @NotNull RuntimeException ignored) {
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerCommandPreprocess(final @NotNull PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        final BaseComponent[] playerVerifyMessage = Utils.playerVerifyMessage.get(player);

        if (playerVerifyMessage != null) {
            player.spigot().sendMessage(playerVerifyMessage);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerMove(final @NotNull PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        final BaseComponent[] playerVerifyMessage = Utils.playerVerifyMessage.get(player);

        if (playerVerifyMessage != null) {
            // 防止卡视角和卡空中
            final Location from = e.getFrom();
            final Location to = e.getTo();
            if (from.getBlockX() == Objects.requireNonNull(to).getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }

            player.spigot().sendMessage(playerVerifyMessage);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final BaseComponent[] playerVerifyMessage = Utils.playerVerifyMessage.get(player);

        if (playerVerifyMessage != null) {
            player.spigot().sendMessage(playerVerifyMessage);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerDropItem(final @NotNull PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        final BaseComponent[] playerVerifyMessage = Utils.playerVerifyMessage.get(player);

        if (playerVerifyMessage != null) {
            player.spigot().sendMessage(playerVerifyMessage);
            e.setCancelled(true);
        }
    }

    // 防止玩家在反作弊验证过程中被攻击
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelPlayerDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player && Utils.playerVerifyCode.containsKey((Player) entity)) {
            e.setCancelled(true);
        }
    }
}
