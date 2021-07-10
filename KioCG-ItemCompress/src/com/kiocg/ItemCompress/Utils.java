package com.kiocg.ItemCompress;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Utils {
    // 存储物品Material、中文名映射
    private static final Map<String, String> I18NDisplayName = new HashMap<>();

    private static final List<String> multipleText = new ArrayList<>() {{
        add("一");
        add("二");
        add("三");
        add("四");
        add("五");
        add("六");
        add("七");
        add("八");
        add("九");
    }};

    public void loadConfig() {
        final FileConfiguration config = ItemCompress.instance.getConfig();

        for (final String key : Objects.requireNonNull(config.getConfigurationSection("")).getKeys(false)) {
            I18NDisplayName.put(key, config.getString(key));
        }
    }

    public static @Nullable String upMultiple(final @NotNull String multiple) {
        try {
            return multipleText.get(multipleText.indexOf(multiple) + 1);
        } catch (final @NotNull IndexOutOfBoundsException ignore) {
            return null;
        }
    }

    public static @Nullable String downMultiple(final @NotNull String multiple) {
        try {
            return multipleText.get(multipleText.indexOf(multiple) - 1);
        } catch (final @NotNull IndexOutOfBoundsException ignore) {
            return null;
        }
    }

    public static int getCustomModelData(final @NotNull String multiple) {
        return multipleText.indexOf(multiple) + 1261;
    }

    public static @NotNull String getI18NDisplayName(final @NotNull String materialString) {
        final String displayName = I18NDisplayName.get(materialString.toLowerCase());
        return displayName == null ? materialString : displayName;
    }
}
