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
    private double total_points;
    private StringStore claimed;
    private HashMap<String,Double> current;
    private HashMap<String,Double> history;

    public GuildActivityData(String guild) {
        this.guild = guild;
        this.points = 0.0;
        this.total_points = 0.0;
        this.claimed = new StringStore();
        this.current = new HashMap<>();
        this.history = new HashMap<>();
    }

    public GuildActivityData(String guild, double points, double total_points, StringStore claimed, HashMap<String, Double> current, HashMap<String, Double> history) {
        this.guild = guild;
        this.points = points;
        this.total_points = total_points;
        this.claimed = claimed;
        this.current = current;
        this.history = history;
    }

    public void clearPlayerData(String player) {
        current.remove(player);
        history.remove(player);
    }
    public double getPlayerActivity(String player) {
        return current.getOrDefault(player,0.0);
    }
    public double getPlayerTotalActivity(String player) {
        return history.getOrDefault(player,0.0);
    }
    public void setPlayerActivity(String player,double amount) {
        current.put(player,amount);
    }
    public void setPlayerHistoryActivity(String player,double amount) {
        history.put(player,amount);
    }

    public double getTotal_points() {
        return total_points;
    }

    public HashMap<String, Double> getCurrent() {
        return current;
    }

    public void setCurrent(HashMap<String, Double> current) {
        this.current = current;
    }

    public void setHistory(HashMap<String, Double> history) {
        this.history = history;
    }

    public HashMap<String, Double> getHistory() {
        return history;
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

    public void setTotal_points(double total_points) {
        this.total_points = total_points;
    }


    public void setClaimed(StringStore claimed) {
        this.claimed = claimed;
    }

    public StringStore getClaimed() {
        return claimed;
    }

    public void update(){
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
