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
import java.util.HashMap;
import java.util.Map;

public class Events implements @NotNull Listener {
    // 存储正在进行反作弊验证的玩家、验证码
    private final Map<Player, String> playerVerifyCode = new HashMap<>();
    // 存储正在进行反作弊验证的玩家、验证提示信息
    private final Map<Player, Component> playerVerifyMessage = new HashMap<>();

    @EventHandler
    public void startPlayerVerify(final @NotNull PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (player.hasPermission("kiocg.anticheatingclient.bypass")) {
            return;
        }

        final String verifyCode = Utils.getNewVerifyCode();
        final Component verifyMessage = Utils.getVerifyMessage(verifyCode);
        playerVerifyCode.put(player, verifyCode);
        playerVerifyMessage.put(player, verifyMessage);
        Bukkit.getScheduler().runTaskLaterAsynchronously(AntiCheatingClient.INSTANCE, () -> player.sendMessage(verifyMessage), 10L);
    }

    @EventHandler
    public void cancelPlayerVerify(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        playerVerifyCode.remove(player);
        playerVerifyMessage.remove(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerVerify(final @NotNull AsyncChatEvent e) {
        final String message = PlainComponentSerializer.plain().serialize(e.message());
        if (message.length() < 192) {
            return;
        }

        // 消息文本长度>=192即取消, 无论是否在进行反作弊验证
        e.setCancelled(true);

        final Player player = e.getPlayer();
        if (!playerVerifyCode.containsKey(player)) {
            return;
        }

        if (message.equals(playerVerifyCode.get(player))) {
            playerVerifyCode.remove(player);
            playerVerifyMessage.remove(player);
            player.sendMessage("§a[§b豆渣子§a] §2你好像很健康呐, 可以开始异世界的探险了~");
        } else if (message.equals(playerVerifyCode.get(player).substring(5))) {
            // 临时封禁玩家1小时
            final Date date = new Date();
            date.setTime(date.getTime() + 1000L * 60L * 60L);
            Bukkit.getScheduler().runTask(AntiCheatingClient.INSTANCE, () -> player.banPlayer("§7... §c快关掉快关掉 作弊可不是好孩子 §7...", date));
            // 提醒全体玩家和群
            final String playerName = player.getName();
            for (final Player toPlayer : Bukkit.getOnlinePlayers()) {
                toPlayer.sendMessage("§7[§b豆渣子§7] §c邪恶生物 " + playerName + " 被安全检查拦截了.");
            }
            AntiCheatingClient.INSTANCE.getLogger().info("§c邪恶生物 " + playerName + " 被安全检查拦截了.");
            try {
                KioCGBot.getApi().sendGroupMsg(569696336L, "邪恶生物 " + playerName + " 被安全检查拦截了.");
            } catch (final @NotNull Exception ignored) {
            }
        } else {
            player.sendMessage("§7[§b豆渣子§7] §6不要修改文本内容呢.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerCommandPreprocess(final @NotNull PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        if (playerVerifyCode.containsKey(player)) {
            player.sendMessage(playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerMove(final @NotNull PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if (playerVerifyCode.containsKey(player)) {
            // 转动视角也会触发PlayerMoveEvent, 防止卡视角和卡空中
            final Location from = e.getFrom();
            final Location to = e.getTo();
            if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }

            player.sendMessage(playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerInteract(final @NotNull PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (playerVerifyCode.containsKey(player)) {
            player.sendMessage(playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerDropItem(final @NotNull PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        if (playerVerifyCode.containsKey(player)) {
            player.sendMessage(playerVerifyMessage.get(player));
            e.setCancelled(true);
        }
    }

    // 防止玩家在反作弊验证过程中被攻击
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void cancelPlayerDamageByEntity(final @NotNull EntityDamageByEntityEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player && playerVerifyCode.containsKey(entity)) {
            e.setCancelled(true);
        }
    }
}
