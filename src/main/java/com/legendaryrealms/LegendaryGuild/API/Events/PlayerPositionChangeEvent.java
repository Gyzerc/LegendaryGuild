package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPositionChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private User user;
    private String oldPositionId;
    private String newPositionId;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public PlayerPositionChangeEvent(User user, String oldPositionId, String newPositionId) {
        this.user = user;
        this.oldPositionId = oldPositionId;
        this.newPositionId = newPositionId;
    }

    public User getUser() {
        return user;
    }

    public String getOldPositionId() {
        return oldPositionId;
    }

    public String getNewPositionId() {
        return newPositionId;
    }
}
