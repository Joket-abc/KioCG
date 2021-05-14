package com.kiocg.BotExtend.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpsUtils {
    // 调用API查询玩家正版UUID
    public static @Nullable String getPlayerUUIDFromApi(final @NotNull String playerName) {
        //noinspection OverlyBroadCatchBlock
        try {
            final URL url = new URL("https://playerdb.co/api/player/minecraft/" + playerName);
            final URLConnection urlCon = url.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(5000);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            final String json = reader.readLine();
            reader.close();

            if (json.contains("\"success\":false")) {
                return null;
            } else {
                final int at = json.indexOf("\"id\":\"");
                return json.substring(at + 6, at + 6 + 36);
            }
        } catch (final @NotNull IOException ignore) {
            return null;
        }
    }
}
