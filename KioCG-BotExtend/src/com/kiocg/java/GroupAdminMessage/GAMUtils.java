package com.kiocg.java.GroupAdminMessage;

import com.kiocg.java.BotExtend;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GAMUtils {
    final static Map<Long, String> GroupLabel = new HashMap<>();

    public boolean isGroupAdmin(final long groupID, final long senderID) {
        try {
            if (Objects.requireNonNull(BotExtend.getInstance().getConfig().getConfigurationSection("groups")).contains(String.valueOf(groupID))) {
                return BotExtend.getInstance().getConfig().getStringList("owners").contains(String.valueOf(senderID))
                        || BotExtend.getInstance().getConfig().getStringList("groups." + groupID + ".admins").contains(String.valueOf(senderID));
            }
            return false;
        } catch (final NullPointerException ignore) {
            return false;
        }
    }

    public static @NotNull String getGroupLabel(final long groupID) {
        if (!GroupLabel.containsKey(groupID)) {
            GroupLabel.put(groupID, Objects.requireNonNull(BotExtend.getInstance().getConfig().getString("groups." + groupID + ".command", "/")));
        }
        return GroupLabel.get(groupID);
    }
}
