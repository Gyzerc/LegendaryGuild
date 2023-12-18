package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildTreeExpChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Guild guild;
    private double amount;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public GuildTreeExpChangeEvent(Guild guild, double amount) {
        this.guild = guild;
        this.amount = amount;
    }

    public Guild getGuild() {
        return guild;
    }

    public double getAmount() {
        return amount;
    }
}
