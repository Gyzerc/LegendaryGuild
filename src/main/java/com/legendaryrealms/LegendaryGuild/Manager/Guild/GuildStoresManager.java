package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.util.HashMap;

public class GuildStoresManager {
    private LegendaryGuild legendaryGuild;
    private HashMap<String, GuildStore> cache;

    public GuildStoresManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new HashMap<>();
    }
    public GuildStore getStore(String guild){
        if (cache.containsKey(guild)){
            return cache.get(guild);
        }
        GuildStore store = legendaryGuild.getDataBase().getStore(guild);
        if (store != null){
            cache.put(guild,store);
            return store;
        }
        return new GuildStore(guild,new HashMap<>());
    }
    public void update(GuildStore store){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveStore(store);
                cache.put(store.getGuild(),store);
            }
        });

    }

    public void reload(String guild){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                cache.put(guild,legendaryGuild.getDataBase().getStore(guild));
            }
        });

    }
    public void loadStores(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getGuildsManager().getGuilds().forEach(guild -> cache.put(guild,legendaryGuild.getDataBase().getStore(guild)));
            }
        });
    }
}
