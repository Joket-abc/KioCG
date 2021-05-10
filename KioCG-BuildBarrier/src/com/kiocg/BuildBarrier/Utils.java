package com.kiocg.BuildBarrier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    // 存储所有开启屏障观察模式的玩家
    public static final List<Player> barrierPlayers = new ArrayList<>();

    public static void startBarrier(final Player player) {
        sendChangeGamemodePacket(player, 1.0F);
    }

    public static void stopBarrier(final Player player) {
        sendChangeGamemodePacket(player, 0.0F);
    }

    private static void sendChangeGamemodePacket(final Player player, final float gamemode) {
        final PacketContainer packet = BuildBarrier.instance.protocolManager.createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);
        packet.getBytes().write(0, (byte) 3);
        packet.getFloat().write(0, gamemode);

        try {
            BuildBarrier.instance.protocolManager.sendServerPacket(player, packet);
        } catch (final @NotNull InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
