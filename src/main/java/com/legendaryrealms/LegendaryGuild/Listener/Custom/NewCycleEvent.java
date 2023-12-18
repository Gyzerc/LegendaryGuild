package com.legendaryrealms.LegendaryGuild.Listener.Custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewCycleEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private int id;
    private int value;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public NewCycleEvent(int id,int value) {
        this.id = id;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}
