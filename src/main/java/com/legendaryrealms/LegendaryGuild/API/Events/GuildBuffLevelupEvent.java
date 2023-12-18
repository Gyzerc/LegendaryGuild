package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildBuffLevelupEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Guild guild;
    private Buff buff;
    private int amount;

    public GuildBuffLevelupEvent(Guild guild, Buff buff,int amount) {
        this.guild = guild;
        this.buff = buff;
        this.amount = amount;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public Guild getGuild() {
        return guild;
    }

    public int getAmount() {
        return amount;
    }

    public Buff getBuff() {
        return buff;
    }
}
