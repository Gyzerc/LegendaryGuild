package com.legendaryrealms.LegendaryGuild.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildHomeTeleportEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private boolean cancel = false;
    private Player player;

    public GuildHomeTeleportEvent(Player player) {
        this.player = player;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    public Player getPlayer() {
        return player;
    }
}
