package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuilTreeWishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player p;
    private Guild guild;

    public GuilTreeWishEvent(Player p, Guild guild) {
        this.p = p;
        this.guild = guild;
    }

    public Player getPlayer() {
        return p;
    }

    public Guild getGuild() {
        return guild;
    }
}
