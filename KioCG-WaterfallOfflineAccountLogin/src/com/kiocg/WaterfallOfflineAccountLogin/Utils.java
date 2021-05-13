package com.kiocg.WaterfallOfflineAccountLogin;

import java.util.ArrayList;
import java.util.List;

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

        WaterfallOfflineAccountLogin.playersFileConfiguration.set("password", playerAndPassword);
        WaterfallOfflineAccountLogin.instance.savePlayersFile();
    }
}
