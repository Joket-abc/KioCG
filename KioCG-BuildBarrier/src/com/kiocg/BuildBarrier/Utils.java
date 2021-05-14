package com.kiocg.BuildBarrier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // 存储所有开启屏障观察模式的玩家
    public static final List<Player> barrierPlayers = new ArrayList<>();

    public static void startBarrier(final @NotNull Player player) {
        //        sendChangeGamemodePacket(player, 1.0F);
        sendBlockChange(player);
    }

    public static void stopBarrier(final @NotNull Player player) {
        //        sendChangeGamemodePacket(player, 0.0F);
        sendBlockChange(player);
    }

    @SuppressWarnings("ImplicitNumericConversion")
    private static void sendBlockChange(final @NotNull Player player) {
        final Location location = player.getLocation();
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY();
        final int blockZ = location.getBlockZ();
        final World world = location.getWorld();

        for (int x = blockX - 32; x <= blockX + 32; ++x) {
            for (int y = blockY - 32; y <= blockY + 32; ++y) {
                for (int z = blockZ - 32; z <= blockZ + 32; ++z) {
                    final Location loc = new Location(world, x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.BARRIER) {
                        player.sendBlockChange(loc, Bukkit.createBlockData(Material.BEDROCK));
                    }
                }
            }
        }
    }

    //    private static void sendChangeGamemodePacket(final Player player, final float gamemode) {
    //        final PacketContainer packet = BuildBarrier.instance.protocolManager.createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);
    //        packet.getBytes().write(0, (byte) 3);
    //        packet.getFloat().write(0, gamemode);
    //
    //        try {
    //            BuildBarrier.instance.protocolManager.sendServerPacket(player, packet);
    //        } catch (final @NotNull InvocationTargetException e) {
    //            e.printStackTrace();
    //        }
    //    }
}
