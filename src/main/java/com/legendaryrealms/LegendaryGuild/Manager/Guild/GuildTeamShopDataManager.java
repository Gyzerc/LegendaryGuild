package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildTeamShopData;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;

import java.util.HashMap;

public class GuildTeamShopDataManager {
    private HashMap<String, GuildTeamShopData> caches;
    private LegendaryGuild legendaryGuild;

    public GuildTeamShopDataManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        caches = new HashMap<>();
    }
    public GuildTeamShopData getGuildTeamShopData(String guild) {
        GuildTeamShopData data = caches.get(guild);
        if (data == null) {
            data = legendaryGuild.getDataBase().getGuildTeamShopData(guild).orElse(new GuildTeamShopData(guild,"",0,new HashMap<>(),new HashMap<>()));
            caches.put(guild,data);
        }
        return data;
    }
    public void removeIfCached(String guild) {
        caches.remove(guild);
    }
    public void update(GuildTeamShopData data,boolean r) {
        legendaryGuild.getDataBase().setGuildTeamShopData(data);



        if (r) {
            caches.remove(data.getGuild());
            return;
        }
        caches.put(data.getGuild(),data);
    }
}
