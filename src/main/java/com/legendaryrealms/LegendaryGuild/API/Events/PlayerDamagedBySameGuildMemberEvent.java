package com.legendaryrealms.LegendaryGuild.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDamagedBySameGuildMemberEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player entity;
    private Player damager;
    private boolean isCanceled;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public PlayerDamagedBySameGuildMemberEvent(Player entity, Player damager, boolean isCanceled) {
        this.entity = entity;
        this.damager = damager;
        this.isCanceled = isCanceled;
    }

    public Player getEntity() {
        return entity;
    }

    public Player getDamager() {
        return damager;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
