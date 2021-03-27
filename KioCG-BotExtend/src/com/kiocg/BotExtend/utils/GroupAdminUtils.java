package com.kiocg.BotExtend.utils;

import com.kiocg.BotExtend.BotExtend;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GroupAdminUtils {
    // 存储所有群、管理员列表
    private static final Map<Long, ArrayList<Long>> groupAdmins = new HashMap<>();
    // 存储所有群、指令前缀列表
    private static final Map<Long, ArrayList<String>> groupLabels = new HashMap<>();

    // 存储logCommand消息
    public static @NotNull String logCommand = "";

    public void loadConfig() {
        final FileConfiguration config = BotExtend.instance.getConfig();
        final List<Long> owners = config.getLongList("owners");

        for (final String group : Objects.requireNonNull(config.getConfigurationSection("groups")).getKeys(false)) {
            groupLabels.put(Long.valueOf(group), (ArrayList<String>) config.getStringList("groups." + group + ".labels"));

            final ArrayList<Long> groupAdmins = new ArrayList<>(owners);
            groupAdmins.addAll(config.getLongList("groups." + group + ".admins"));
            GroupAdminUtils.groupAdmins.put(Long.valueOf(group), groupAdmins);
        }

        logCommand = Objects.requireNonNull(config.getString("messages.logCommand", "&cQQ用户 %user% 执行了指令: %cmd%"));
    }

    public static boolean isGroupAdmin(final long groupID, final long senderID) {
        try {
            return groupAdmins.get(groupID).contains(senderID);
        } catch (final @NotNull NullPointerException ignore) {
            return false;
        }
    }

    public static @Nullable ArrayList<String> getGroupLabels(final long groupID) {
        return groupLabels.get(groupID);
    }
}
