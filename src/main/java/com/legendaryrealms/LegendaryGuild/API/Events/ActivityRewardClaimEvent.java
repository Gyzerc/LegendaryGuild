package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Others.ActivityReward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ActivityRewardClaimEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Player p;
    private ActivityReward activityReward;

    public ActivityRewardClaimEvent(Player p, ActivityReward activityReward) {
        this.p = p;
        this.activityReward = activityReward;
    }

    public Player getP() {
        return p;
    }

    public ActivityReward getActivityReward() {
        return activityReward;
    }
}
