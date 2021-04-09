package com.kiocg.AntiCheatingClient;

import com.kiocg.qqBot.bot.KioCGBot;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Listeners implements @NotNull Listener {
    @EventHandler
    public void startPlayerVerify(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        if (player.hasPermission("kiocg.anticheatingclient.bypass")) {
            return;
        }

        final String verifyCode = Utils.getNewVerifyCode();
        final Component verifyMessage = Utils.getVerifyMessage(verifyCode);

        Utils.playerVerifyCode.put(player, verifyCode);
        Utils.playerVerifyMessage.put(player, verifyMessage);

        // 延迟10tick防止信息被忽视
        Bukkit.getScheduler().runTaskLaterAsynchronously(AntiCheatingClient.instance, () -> player.sendMessage(verifyMessage), 10L);
    }

    @EventHandler
    public void cancelPlayerVerify(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        Utils.playerVerifyCode.remove(player);
        Utils.playerVerifyMessage.remove(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerVerify(final @NotNull AsyncChatEvent e) {
        final String message = PlainComponentSerializer.plain().serialize(e.message());

        if (!message.startsWith(".say ")) {
            return;
        }

        // 消息为反作弊前缀即取消, 无论是否在进行反作弊验证
        e.setCancelled(true);

        final Player player = e.getPlayer();

        if (!Utils.playerVerifyCode.containsKey(player)) {
            return;
        }

        if (message.equals(Utils.playerVerifyCode.get(player))) {
            Utils.playerVerifyCode.remove(player);
            Utils.playerVerifyMessage.remove(player);

            player.sendMessage("§a[§b豆渣子§a] §2你好像很健康呐, 可以开始异世界的探险了~");
        } else if (message.equals(Utils.playerVerifyCode.get(player).substring(5))) {
            // 临时封禁玩家24小时
            final Date date = new Date();
            date.setTime(date.getTime() + 1000L * 60L * 60L * 24L);
            Bukkit.getScheduler().runTask(AntiCheatingClient.instance, () -> player.banPlayer("§7... §c快关掉快关掉 作弊可不是好孩子 §7...", date));

            final String playerName = player.getName();

            // 提醒全体玩家
            for (final Player toPlayer : Bukkit.getOnlinePlayers()) {
                toPlayer.sendMessage("§a[§b豆渣子§a] §c邪恶生物 " + playerName + " 被安全检查拦截了.");
            }
            // 控制台记录
            AntiCheatingClient.instance.getLogger().info("§c邪恶生物 " + playerName + " 被安全检查拦截了.");

            // 提醒全体群成员
            try {
                KioCGBot.sendGroupMsgAsync(569696336L, "邪恶生物 " + playerName + " 被安全检查拦截了.");
            } catch (final @NotNull Exception ignored) {
            }
        } else {
            player.sendMessage("§a[§b豆渣子§a] §6不要修改文本内容呢.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerCommandPreprocess(final @NotNull PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        if (Utils.playerVerifyCode.containsKey(player)) {
            player.sendMessage(Utils.playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerMove(final @NotNull PlayerMoveEvent e) {
        final Player player = e.getPlayer();

        if (Utils.playerVerifyCode.containsKey(player)) {
            // 防止卡视角和卡空中
            final Location from = e.getFrom();
            final Location to = e.getTo();
            if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }

            player.sendMessage(Utils.playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (Utils.playerVerifyCode.containsKey(player)) {
            player.sendMessage(Utils.playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerDropItem(final @NotNull PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        if (Utils.playerVerifyCode.containsKey(player)) {
            player.sendMessage(Utils.playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    // 防止玩家在反作弊验证过程中被攻击
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelPlayerDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player && Utils.playerVerifyCode.containsKey(entity)) {
            e.setCancelled(true);
        }
    }
}
