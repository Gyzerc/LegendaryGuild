package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Level;

public class GuildActivityDataManager {
    private HashMap<String, GuildActivityData> cache;
    private LegendaryGuild legendaryGuild;

    public GuildActivityDataManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new HashMap<>();
    }

    public void removeAll(){
        cache = new HashMap<>();
    }
    public GuildActivityData getData(String guild){
        if (cache.containsKey(guild)){
            return cache.get(guild);
        }
        GuildActivityData data = legendaryGuild.getDataBase().getGuildActivityData(guild).orElse(new GuildActivityData(guild));
        cache.put(guild,data);
        return data;
    }

    public void reloadGuildIfCached(String guild){
        if (cache.containsKey(guild)) {
            cache.put(guild, legendaryGuild.getDataBase().getGuildActivityData(guild).orElse(new GuildActivityData(guild)));
        }
    }
    public void removeGuildCache(String guild){
        cache.remove(guild);
    }
    public void updataGuildActivityData(GuildActivityData data,boolean removeCache){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveGuildActivityData(data);
                if (removeCache){
                    cache.remove(data.getGuild());
                }
            }
        });
    }


    public void checkCycle(){
        int targetInt = legendaryGuild.getFileManager().getConfig().ACTIVITY_CYCLE;
        if (targetInt > 0) {
            int value = Integer.parseInt(legendaryGuild.getDataBase().getSystemData("activity_day").orElse("0"));
            int set = value + 1;
            if (value >= (targetInt - 1)) {
                set = 0;
                legendaryGuild.info("刷新所有公会活跃度", Level.INFO);
                legendaryGuild.info("Refresh all guild activity levels", Level.INFO);
                resetAllGuild();
//            Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
//            if (p != null) {
//                new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
//                        .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.REFRESH_ACTIVITY, "null"))
//                        .setReciver("ALL")
//                        .sendPluginMessage(p);
//            }
            }
            legendaryGuild.getDataBase().saveSystemData("activity_day", set + "");
        }
    }

    private void resetAllGuild(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                for (String guild : legendaryGuild.getDataBase().getGuildActivityDatas()){
                    GuildActivityData data = getData(guild);
                    data.setPoints(0);
                    data.setCurrent(new HashMap<>());
                    data.setClaimed(new StringStore());
                    data.update();
                }
            }
        });

    }
}
