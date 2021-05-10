package com.kiocg.BuildBarrier;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BuildBarrier extends JavaPlugin {
    public static BuildBarrier instance;

    public ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        Objects.requireNonNull(getServer().getPluginCommand("barrier")).setExecutor(new BarrierCommand());
    }

    @Override
    public void onDisable() {
        for (final Player player : Utils.barrierPlayers) {
            Utils.stopBarrier(player);
            player.sendMessage("§a[§b豆渣子§a] §c插件重载迫使你关闭了屏障观察模式.");
        }
    }
}
