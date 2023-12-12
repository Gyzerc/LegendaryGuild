package com.legendaryrealms.LegendaryGuild.Manager;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.*;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildShopPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MenuLoadersManager {
    private HashMap<String, MenuLoader> cache;
    private LegendaryGuild legendaryGuild;
    public MenuLoadersManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        cache = new HashMap<>();

        //注册GUI
        registerAll();
    }

    public void registerAll(){

        cache.put("GuildList",new GuildListLoader(legendaryGuild));
        cache.put("Applications",new ApplicationsLoader(legendaryGuild));
        cache.put("Members",new MembersLoader(legendaryGuild));
        if (legendaryGuild.getFileManager().getStores().isEnable()) {
            cache.put("Stores", new StoresLoader(legendaryGuild));
        }
        cache.put("RedPackets",new RedPacketsLoader(legendaryGuild));
        cache.put("GuildTree",new GuildTreeLoader(legendaryGuild));
        cache.put("GuildIconsShop",new GuildIconsShopLoader(legendaryGuild));
        cache.put("Tributes",new TributesLoader(legendaryGuild));
        cache.put("GuildShop",new GuildShopLoader(legendaryGuild));
        if (legendaryGuild.getBuffsManager().isEnable()){
            cache.put("Buff",new BuffLoader(legendaryGuild));
        }
    }

    public List<String> getMenuIds(){
        return new ArrayList<>(cache.keySet());
    }

    public MenuLoader getMenuLoader(String id){
        return cache.get(id);
    }
}
