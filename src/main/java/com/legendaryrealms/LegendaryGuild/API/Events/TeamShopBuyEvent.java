package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamShopBuyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player player;
    private String id;

    public TeamShopBuyEvent(Player player, String id) {
        this.player = player;
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }
}
