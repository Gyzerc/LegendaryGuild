package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import sun.dc.pr.PRError;

public class GuildHomeChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Player setter;
    private Guild guild;
    private Guild.GuildHomeLocation guildHomeLocation;

    public GuildHomeChangeEvent(Player setter, Guild guild, Guild.GuildHomeLocation guildHomeLocation) {
        this.setter = setter;
        this.guild = guild;
        this.guildHomeLocation = guildHomeLocation;
    }

    public Player getSetter() {
        return setter;
    }

    public Guild getGuild() {
        return guild;
    }

    public Guild.GuildHomeLocation getGuildHomeLocation() {
        return guildHomeLocation;
    }
}
