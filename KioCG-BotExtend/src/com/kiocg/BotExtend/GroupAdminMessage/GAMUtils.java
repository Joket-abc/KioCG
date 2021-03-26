package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.BotExtend.BotExtend;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GAMUtils {
    // 存储所有群管理员的缓存
    private static final Map<Long, ArrayList<Long>> groupAdmin = new HashMap<>();
    // 存储所有群指令前缀的缓存
    private static final Map<Long, ArrayList<String>> groupLabel = new HashMap<>();

    // 存储log_command消息
    public static @NotNull String logCommand = "";

    public void loadConfig() {
        final FileConfiguration config = BotExtend.INSTANCE.getConfig();
        final List<Long> owners = config.getLongList("owners");
        for (final String group : Objects.requireNonNull(config.getConfigurationSection("groups")).getKeys(false)) {
            groupLabel.put(Long.valueOf(group), (ArrayList<String>) config.getStringList("groups." + group + ".command"));
            final ArrayList<Long> groupAdmins = new ArrayList<>(owners);
            groupAdmins.addAll(config.getLongList("groups." + group + ".admins"));
            groupAdmin.put(Long.valueOf(group), groupAdmins);
        }
        logCommand = Objects.requireNonNull(config.getString("messages.log_command", "&cQQ用户 %user% 执行了 %cmd% 指令."));
    }

    public static boolean isGroupAdmin(final long groupID, final long senderID) {
        try {
            return groupAdmin.get(groupID).contains(senderID);
        } catch (final @NotNull NullPointerException ignore) {
            return false;
        }
    }

    public static @Nullable ArrayList<String> getGroupLabels(final long groupID) {
        return groupLabel.get(groupID);
    }
}
