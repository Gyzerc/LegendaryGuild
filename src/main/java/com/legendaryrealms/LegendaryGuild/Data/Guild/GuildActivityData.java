package com.legendaryrealms.LegendaryGuild.Data.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class GuildActivityData {
    private String guild;
    private double points;
    private StringStore claimed;

    public GuildActivityData(String guild) {
        this.guild = guild;
        this.points = 0.0;
        this.claimed = new StringStore();
    }

    public GuildActivityData(String guild, double points, StringStore claimed) {
        this.guild = guild;
        this.points = points;
        this.claimed = claimed;
    }

    public String getGuild() {
        return guild;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setClaimed(StringStore claimed) {
        this.claimed = claimed;
    }

    public StringStore getClaimed() {
        return claimed;
    }

    public void updata(){
        LegendaryGuild.getInstance().getGuildActivityDataManager().updataGuildActivityData(this,false);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_GUILD_ACTIVITY_DATA, guild))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
        }
    }
}
