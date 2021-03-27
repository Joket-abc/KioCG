package com.kiocg.ItemCompress;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {
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
        add("十");
    }};

    public static @NotNull String upMultiple(final @NotNull String multiple) {
        try {
            return multipleText.get(multipleText.indexOf(multiple) + 1);
        } catch (final @NotNull IndexOutOfBoundsException ignore) {
            return "";
        }
    }

    public static @NotNull String downMultiple(final @NotNull String multiple) {
        try {
            return multipleText.get(multipleText.indexOf(multiple) - 1);
        } catch (final @NotNull IndexOutOfBoundsException ignore) {
            return "";
        }
    }
}
