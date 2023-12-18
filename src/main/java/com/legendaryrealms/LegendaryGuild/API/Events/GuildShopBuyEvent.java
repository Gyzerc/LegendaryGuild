package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildShopBuyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player p;
    private ShopItem shopItem;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public GuildShopBuyEvent(Player p, ShopItem shopItem) {
        this.p = p;
        this.shopItem = shopItem;
    }

    public Player getPlayer() {
        return p;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }
}
