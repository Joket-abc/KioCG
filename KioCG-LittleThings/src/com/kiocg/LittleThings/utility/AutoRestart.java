package com.kiocg.LittleThings.utility;

import com.kiocg.LittleThings.LittleThings;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AutoRestart {
    public AutoRestart() {
        autoRestart(new HashMap<>() {{
            put("05:00:00", "1小时");
            put("05:50:00", "10分钟");
            put("05:59:00", "1分钟");
            put("05:59:50", "10秒");
            put("05:59:55", "5秒");
            put("05:59:56", "4秒");
            put("05:59:57", "3秒");
            put("05:59:58", "2秒");
            put("05:59:59", "1秒");
        }});
    }

    // 自动重启
    private void autoRestart(final @NotNull Map<String, String> autoRestartMessage) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LittleThings.instance, () -> {
            final String date = new SimpleDateFormat("HH:mm:ss").format(new Date());

            if ("06:00:00".equals(date)) {
                Bukkit.getScheduler().runTask(LittleThings.instance, () -> {
                    Bukkit.getOnlinePlayers().forEach(player -> player.kick(LegacyComponentSerializer.legacyAmpersand().deserialize("§7... §c少女祈祷中 §7...\n§f \n§8(AutoRestart)")));
                    Bukkit.shutdown();
                });
                return;
            }

            if (autoRestartMessage.containsKey(date)) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendTitle("§c世界正在破裂...", "§6将在 §e" + autoRestartMessage.get(date) + " §6后自动重启!", 10, 70, 20);
                    player.sendMessage("§a[§b豆渣子§a] §c世界正在破裂... §6将在 §e" + autoRestartMessage.get(date) + " §6后自动重启!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0F, 1.0F);
                });

                autoRestartMessage.remove(date);
            }
            // 每19tick循环防止tps小于20时导致错过时间
        }, 19L, 19L);
    }
}
