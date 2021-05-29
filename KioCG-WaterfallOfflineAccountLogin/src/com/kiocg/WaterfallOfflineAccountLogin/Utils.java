package com.kiocg.WaterfallOfflineAccountLogin;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    // 存储玩家-密码
    private static final Set<String> playerAndPasswords = new HashSet<>();
    // 存储IP、密码错误次数
    public static final Map<String, Integer> wrongPasswordIPs = new HashMap<>();

    public void loadPlayers() {
        playerAndPasswords.addAll(WaterfallOfflineAccountLogin.playersFileConfiguration.getStringList("password"));
    }

    public static Boolean isPasswordTrue(final String playerAndPassword) {
        return playerAndPasswords.contains(playerAndPassword);
    }

    public static String splitPlayerName(final String playerAndPassword) {
        return playerAndPassword.split("-")[0];
    }

    public static void addPlayer(final String playerAndPassword) {
        playerAndPasswords.add(playerAndPassword);

        WaterfallOfflineAccountLogin.playersFileConfiguration.set("password", playerAndPasswords);
        WaterfallOfflineAccountLogin.instance.savePlayersFile();
    }

    // 返回是否为合法的专用离线玩家名
    public static Boolean isLegalPlayerName(final String string) {
        return Pattern.matches("^[0-9a-zA-Z_]{3,14}$", string);
    }

    // 返回账号-随机密码 player-password
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
