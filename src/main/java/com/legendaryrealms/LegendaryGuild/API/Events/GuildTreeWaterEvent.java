package com.legendaryrealms.LegendaryGuild.API.Events;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.WaterPot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildTreeWaterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    private Player p;
    private WaterPot waterPot;
    private Guild guild;
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public GuildTreeWaterEvent(Player p, WaterPot waterPot, Guild guild) {
        this.p = p;
        this.waterPot = waterPot;
        this.guild = guild;
    }

    public Player getPlayer() {
        return p;
    }

    public WaterPot getWaterPot() {
        return waterPot;
    }

    public Guild getGuild() {
        return guild;
    }
}
