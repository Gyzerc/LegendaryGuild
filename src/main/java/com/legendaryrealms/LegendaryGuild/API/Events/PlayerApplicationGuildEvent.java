package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerApplicationGuildEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private User user;
    private Guild guild;

    public PlayerApplicationGuildEvent(User p, Guild guild) {
        this.user = p;
        this.guild = guild;
    }

    public User getPlayer() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }
}
