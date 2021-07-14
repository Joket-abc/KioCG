package com.kiocg.ShopAddon.Listeners;

import com.kiocg.ShopAddon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.event.ShopPurchaseEvent;
import org.maxgamer.quickshop.shop.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LimitDailySales implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onShopPurchase(final @NotNull ShopPurchaseEvent e) {
        final Shop shop = e.getShop();
        if (shop.isSelling() || !"06ea1ffa-d3cf-48a4-856e-23e47d297511".equals(shop.getOwner().toString()) || !shop.getItem().displayName().toString().contains("次压缩")) {
            return;
        }

        final UUID purchaser = e.getPurchaser();
        final Player player = Bukkit.getPlayer(purchaser);

        if (e.getAmount() > 1) {
            Objects.requireNonNull(player).sendMessage("§a[§b豆渣子§a] §6你每天只能出售一个此物品给豆渣子.");
            e.setCancelled(true);
            return;
        }

        final String uuidString = purchaser.toString();
        final List<Long> shopBlockKeys = Utils.playerDailySales.get(uuidString);
        if (shopBlockKeys == null) {
            Utils.playerDailySales.put(uuidString, new ArrayList<>() {{
                add(shop.getLocation().toBlockKey());
            }});
        } else {
            final Long blockKey = shop.getLocation().toBlockKey();
            if (shopBlockKeys.contains(blockKey)) {
                Objects.requireNonNull(player).sendMessage("§a[§b豆渣子§a] §6你每天只能出售一个此物品给豆渣子.");
                e.setCancelled(true);
                return;
            }

            shopBlockKeys.add(blockKey);
        }
    }
}
