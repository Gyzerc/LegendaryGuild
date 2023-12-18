package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildActivityChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Guild guild;
    private ChangeType type;
    private double changeAmount;

    public GuildActivityChangeEvent(Guild guild, ChangeType type, double changeAmount) {
        this.guild = guild;
        this.type = type;
        this.changeAmount = changeAmount;
    }

    public Guild getGuild() {
        return guild;
    }

    public ChangeType getType() {
        return type;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public enum ChangeType {
        Add,
        Take,
        Set;
    }
}
