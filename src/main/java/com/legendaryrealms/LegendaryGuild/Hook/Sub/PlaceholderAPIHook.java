package com.legendaryrealms.LegendaryGuild.Hook.Sub;

import com.legendaryrealms.LegendaryGuild.Hook.Hook;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.logging.Level;

public class PlaceholderAPIHook extends Hook {
    public PlaceholderAPIHook(LegendaryGuild legendaryGuild) {
        super(legendaryGuild);
        enable = getHook();
    }

    public String replaceHolder(String str, OfflinePlayer offlinePlayer){
        if (!enable){return str;}
        return PlaceholderAPI.setPlaceholders(offlinePlayer, str);
    }

    public List<String> replaceHolder(List<String> list,OfflinePlayer offlinePlayer) {
        if (!enable){return list;}
        return PlaceholderAPI.setPlaceholders(offlinePlayer,list);
    }
    @Override
    public boolean getHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            legendaryGuild.info("已关联插件 PlaceholderAPI & Hooked PlaceholderAPI plugin.", Level.INFO);
            return true;
        }
        return false;
    }
}
