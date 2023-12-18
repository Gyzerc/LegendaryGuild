package com.legendaryrealms.LegendaryGuild.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiveGuildMoneyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Player p;
    private double amount;

    public GiveGuildMoneyEvent(Player p, double amount) {
        this.p = p;
        this.amount = amount;
    }

    public Player getP() {
        return p;
    }

    public double getAmount() {
        return amount;
    }
}
