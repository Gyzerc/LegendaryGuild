package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildGiveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player oldOwner;
    private String targetPlayer;
    private Guild guild;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public GuildGiveEvent(Player oldOwner, String targetPlayer, Guild guild) {
        this.oldOwner = oldOwner;
        this.targetPlayer = targetPlayer;
        this.guild = guild;
    }

    public Player getOldOwner() {
        return oldOwner;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public Guild getGuild() {
        return guild;
    }
}
