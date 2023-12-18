package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinGuildEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private User user;
    private Guild guild;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public PlayerJoinGuildEvent(User user, Guild guild) {
        this.user = user;
        this.guild = guild;
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }
}
