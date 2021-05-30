package com.kiocg.PaperOfflineAccountLogin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.base.Charsets;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PaperOfflineAccountLogin extends JavaPlugin {
    @SuppressWarnings("unused")
    public static PaperOfflineAccountLogin instance;

    @Override
    public void onEnable() {
        instance = this;

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.LOWEST, PacketType.Login.Client.START) {
            @Override
            public void onPacketReceiving(final @NotNull PacketEvent e) {
                final StructureModifier<WrappedGameProfile> gameProfiles = e.getPacket().getGameProfiles();
                final String username = gameProfiles.read(0).getName();
                if (username.contains("-")) {
                    final String playerName = username.split("-")[0];
                    final String uuidString = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8)).toString();
                    //noinspection SpellCheckingInspection
                    gameProfiles.write(0, new WrappedGameProfile(UUID.fromString("ffffffff-ffff-ffff" + uuidString.substring(18)), playerName));
                }
            }
        });
    }
}
