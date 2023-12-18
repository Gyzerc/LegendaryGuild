package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildTreeLevelupEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Guild guild;
    private int oldLevel;
    private int newLevel;

    public GuildTreeLevelupEvent(Guild guild, int oldLevel, int newLevel) {
        this.guild = guild;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Guild getGuild() {
        return guild;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
