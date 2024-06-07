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
        register(new GuildListLoader(legendaryGuild));
        register(new ApplicationsLoader(legendaryGuild));
        register(new ApplicationsLoader(legendaryGuild));
        register(new MembersLoader(legendaryGuild));
        if (legendaryGuild.getFileManager().getStores().isEnable()) {
            register(new StoresLoader(legendaryGuild));
        }
        register(new RedPacketsLoader(legendaryGuild));
        register(new GuildTreeLoader(legendaryGuild));
        register(new GuildIconsShopLoader(legendaryGuild));
        register(new TributesLoader(legendaryGuild));
        register(new GuildShopLoader(legendaryGuild));
        if (legendaryGuild.getFileManager().getBuffFile().getEnable()){
            register(new BuffLoader(legendaryGuild));
        }
        register(new ActivityRewardsLoader(legendaryGuild));
        register(new PositionsLoader(legendaryGuild));
        register(new GuildMenuLoader(legendaryGuild));
        register(new TeamShopLoader(legendaryGuild));
    }

    private void register(MenuLoader loader){
        cache.put(loader.getClass().getSimpleName().replace("Loader",""),loader);
    }

    public List<String> getMenuIds(){
        return new ArrayList<>(cache.keySet());
    }

    public MenuLoader getMenuLoader(String id){
        return cache.get(id);
    }
}
