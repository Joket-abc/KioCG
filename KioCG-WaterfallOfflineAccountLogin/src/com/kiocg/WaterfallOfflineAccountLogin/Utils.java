package com.kiocg.WaterfallOfflineAccountLogin;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {
    // 存储玩家-密码
    private static final List<String> playerPasswords = new ArrayList<>();

    public void loadPlayers() {
        playerPasswords.addAll(WaterfallOfflineAccountLogin.playersFileConfiguration.getStringList("password"));
    }

    public static Boolean isPasswordTrue(final String playerAndPassword) {
        return playerPasswords.contains(playerAndPassword);
    }

    public static String splitPlayerName(final String playerAndPassword) {
        return playerAndPassword.split("-")[0];
    }

    public static void addPlayer(final String playerAndPassword) {
        playerPasswords.add(playerAndPassword);

        WaterfallOfflineAccountLogin.playersFileConfiguration.set("password", playerPasswords);
        WaterfallOfflineAccountLogin.instance.savePlayersFile();
    }

    // 返回是否为合法玩家名
    public static Boolean isLegalPlayerName(final String string) {
        return Pattern.matches("^[0-9a-zA-Z_]{3,14}$", string);
    }

    // 返回player-password
    public static String newAccount(final String playerName) {
        final int length = playerName.length();
        if (length < 10) {
            return playerName + "-" + RandomStringUtils.randomAlphanumeric(15 - length);
        }

        return playerName + "-" + getRandomChinese(15 - length);
    }

    private static String getRandomChinese(final int length) {
        final char[] chars = new char[length];
        for (int i = 0; i < length; ++i) {
            chars[i] = (char) (new Random().nextInt(20902) + 19968);
        }
        return new String(chars);
    }
}
