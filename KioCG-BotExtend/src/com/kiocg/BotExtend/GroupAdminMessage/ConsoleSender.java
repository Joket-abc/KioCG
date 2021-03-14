package com.kiocg.BotExtend.GroupAdminMessage;

import com.kiocg.BotExtend.BotExtend;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConsoleSender implements ConsoleCommandSender {
    private final Server server;
    private final GroupMessageEvent event;
    private final List<String> output = new ArrayList<>();
    private BukkitTask task;

    public ConsoleSender(final GroupMessageEvent event) {
        this.server = Bukkit.getServer();
        this.event = event;
        send();
    }

    private void send() {
        if (task != null) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.getInstance(), () -> {
            final StringBuilder output = new StringBuilder();
            for (final String string : this.output) {
                output.append(string).append("\n");
            }
            final String message = output.toString().trim();
            event.getGroup().sendMessage(message.isEmpty() ? "指令已发送" : message);
            this.output.clear();
        }, 4L);
    }

    private Optional<ConsoleCommandSender> get() {
        return Optional.of(server.getConsoleSender());
    }

    @Override
    public void sendMessage(@NotNull final String s) {
        output.add(ChatColor.stripColor(s));
        send();
    }

    @Override
    public void sendMessage(final @NotNull String[] strings) {
        for (final String s : strings) {
            sendMessage(s);
        }
    }

    @Override
    public void sendMessage(@Nullable final UUID uuid, @NotNull final String s) {
        server.getConsoleSender().sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(@Nullable final UUID uuid, final @NotNull String[] strings) {
        server.getConsoleSender().sendMessage(uuid, strings);
    }

    @Override
    public @NotNull Server getServer() {
        return server;
    }

    @Override
    public @NotNull String getName() {
        return "CONSOLE";
    }

    @Override
    public @NotNull Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConversing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptConversationInput(@NotNull final String s) {
    }

    @Override
    public boolean beginConversation(@NotNull final Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(@NotNull final Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(@NotNull final Conversation conversation, @NotNull final ConversationAbandonedEvent conversationAbandonedEvent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRawMessage(@NotNull final String s) {
    }

    @Override
    public void sendRawMessage(@Nullable final UUID uuid, @NotNull final String s) {
    }

    @Override
    public boolean isPermissionSet(@NotNull final String s) {
        return get().map(c -> c.isPermissionSet(s)).orElse(true);
    }

    @Override
    public boolean isPermissionSet(@NotNull final Permission permission) {
        return get().map(c -> c.isPermissionSet(permission)).orElse(true);
    }

    @Override
    public boolean hasPermission(@NotNull final String s) {
        return get().map(c -> c.hasPermission(s)).orElse(true);
    }

    @Override
    public boolean hasPermission(@NotNull final Permission permission) {
        return get().map(c -> c.hasPermission(permission)).orElse(true);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull final Plugin plugin, @NotNull final String s, final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull final Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull final Plugin plugin, @NotNull final String s, final boolean b, final int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull final Plugin plugin, final int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(@NotNull final PermissionAttachment permissionAttachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(final boolean b) {
        throw new UnsupportedOperationException();
    }
}
