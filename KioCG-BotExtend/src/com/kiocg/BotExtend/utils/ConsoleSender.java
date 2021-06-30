package com.kiocg.BotExtend.utils;

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
import java.util.regex.Pattern;

public class ConsoleSender implements ConsoleCommandSender {
    private final Pattern ip = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
    private final @NotNull ConsoleCommandSender consoleCommandSender;
    private final GroupMessageEvent event;

    private BukkitTask task;
    private final List<String> output = new ArrayList<>();

    public ConsoleSender(final GroupMessageEvent event) {
        consoleCommandSender = Bukkit.getConsoleSender();
        this.event = event;

        task = newTask();
    }

    private @NotNull BukkitTask newTask() {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(BotExtend.instance, () -> {
            final StringBuilder stringBuilder = new StringBuilder();

            output.forEach(string -> stringBuilder.append(ChatColor.stripColor(string)).append("\n"));
            output.clear();

            final String message = ip.matcher(stringBuilder.toString().trim()).replaceAll("xxx.xxx.xxx.xxx");
            event.getGroup().sendMessage(message.isEmpty() ? "指令已成功发送，执行结果未知" : message);
        }, 4L);
    }

    @Override
    public void sendMessage(final @NotNull String string) {
        output.add(string);
        task.cancel();
        task = newTask();
    }

    @Override
    public void sendMessage(final @NotNull String[] strings) {
        output.addAll(Arrays.asList(strings));
        task.cancel();
        task = newTask();
    }

    // 分割线 分割线 分割线 分割线 分割线

    @Override
    public void sendMessage(final @Nullable UUID uuid, final @NotNull String string) {
        consoleCommandSender.sendMessage(uuid, string);
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
    public void acceptConversationInput(final @NotNull String string) {
        consoleCommandSender.acceptConversationInput(string);
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
    public void sendRawMessage(final @NotNull String string) {
        consoleCommandSender.sendRawMessage(string);
    }

    @Override
    public void sendRawMessage(final @Nullable UUID uuid, final @NotNull String string) {
        consoleCommandSender.sendRawMessage(uuid, string);
    }

    @Override
    public boolean isPermissionSet(final @NotNull String string) {
        return consoleCommandSender.isPermissionSet(string);
    }

    @Override
    public boolean isPermissionSet(final @NotNull Permission permission) {
        return consoleCommandSender.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(final @NotNull String string) {
        return consoleCommandSender.hasPermission(string);
    }

    @Override
    public boolean hasPermission(final @NotNull Permission permission) {
        return consoleCommandSender.hasPermission(permission);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String string, final boolean b) {
        return consoleCommandSender.addAttachment(plugin, string, b);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(final @NotNull Plugin plugin) {
        return consoleCommandSender.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String string, final boolean b, final int i) {
        return consoleCommandSender.addAttachment(plugin, string, b, i);
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
