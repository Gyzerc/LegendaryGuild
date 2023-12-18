package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Others.TributeItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class GuildTributesEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player p;
    private List<TributeItem> tributeItems;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public GuildTributesEvent(Player p, List<TributeItem> tributeItems) {
        this.p = p;
        this.tributeItems = tributeItems;
    }

    public Player getPlayer() {
        return p;
    }

    public List<TributeItem> getTributeItems() {
        return tributeItems;
    }
}
