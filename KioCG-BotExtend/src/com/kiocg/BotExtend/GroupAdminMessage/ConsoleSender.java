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

public class ConsoleSender implements @NotNull ConsoleCommandSender {
    private final @NotNull ConsoleCommandSender consoleCommandSender;
    private final GroupMessageEvent event;
    private final List<String> output = new ArrayList<>();
    private BukkitTask task;

    public ConsoleSender(final GroupMessageEvent event) {
        this.consoleCommandSender = Bukkit.getConsoleSender();
        this.event = event;
        task = task();
    }

    private @NotNull BukkitTask task() {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.getInstance(), () -> {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final String string : output) {
                stringBuilder.append(ChatColor.stripColor(string)).append("\n");
            }
            output.clear();

            final String message = stringBuilder.toString().trim();
            event.getGroup().sendMessage(message.isEmpty() ? "指令已发送" : message);
        }, 4L);
    }

    @Override
    public void sendMessage(final @NotNull String s) {
        output.add(s);
        task.cancel();
        task = task();
    }

    @Override
    public void sendMessage(final @NotNull String[] strings) {
        output.addAll(Arrays.asList(strings));
        task.cancel();
        task = task();
    }

    @Override
    public void sendMessage(final @Nullable UUID uuid, final @NotNull String s) {
        consoleCommandSender.sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(final @Nullable UUID uuid, final @NotNull String[] strings) {
        consoleCommandSender.sendMessage(uuid, strings);
    }

    @Override
    public @NotNull Server getServer() {
        return consoleCommandSender.getServer();
    }

    @Override
    public @NotNull String getName() {
        return consoleCommandSender.getName();
    }

    @Override
    public @NotNull Spigot spigot() {
        return consoleCommandSender.spigot();
    }

    @Override
    public boolean isConversing() {
        return consoleCommandSender.isConversing();
    }

    @Override
    public void acceptConversationInput(final @NotNull String s) {
        consoleCommandSender.acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(final @NotNull Conversation conversation) {
        return consoleCommandSender.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(final @NotNull Conversation conversation) {
        consoleCommandSender.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(final @NotNull Conversation conversation, final @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        consoleCommandSender.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public void sendRawMessage(final @NotNull String s) {
        consoleCommandSender.sendRawMessage(s);
    }

    @Override
    public void sendRawMessage(final @Nullable UUID uuid, final @NotNull String s) {
        consoleCommandSender.sendRawMessage(uuid, s);
    }

    @Override
    public boolean isPermissionSet(final @NotNull String s) {
        return consoleCommandSender.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(final @NotNull Permission permission) {
        return consoleCommandSender.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(final @NotNull String s) {
        return consoleCommandSender.hasPermission(s);
    }

    @Override
    public boolean hasPermission(final @NotNull Permission permission) {
        return consoleCommandSender.hasPermission(permission);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String s, final boolean b) {
        return consoleCommandSender.addAttachment(plugin, s, b);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(final @NotNull Plugin plugin) {
        return consoleCommandSender.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String s, final boolean b, final int i) {
        return consoleCommandSender.addAttachment(plugin, s, b, i);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(final @NotNull Plugin plugin, final int i) {
        return consoleCommandSender.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(final @NotNull PermissionAttachment permissionAttachment) {
        consoleCommandSender.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        consoleCommandSender.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return consoleCommandSender.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return consoleCommandSender.isOp();
    }

    @Override
    public void setOp(final boolean b) {
        consoleCommandSender.setOp(b);
    }
}
