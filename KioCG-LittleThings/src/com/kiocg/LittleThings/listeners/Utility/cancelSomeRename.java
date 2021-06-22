package com.kiocg.LittleThings.listeners.Utility;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class cancelSomeRename implements Listener {
    // 防止重命名成内部保留的物品前缀名
    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelSomeRename(final @NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            return;
        }

        final String renameText = e.getInventory().getRenameText();

        if (renameText == null) {
            return;
        }

        if (Pattern.matches("^(&[0-9a-zA-Z]){3}.*$", renameText) || Pattern.matches("^(&#[0-9a-zA-Z]{6}){3}.*$", renameText)) {
            e.setResult(null);
        }
    }
}
