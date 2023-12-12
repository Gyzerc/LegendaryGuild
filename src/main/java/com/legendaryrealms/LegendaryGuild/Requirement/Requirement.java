package com.legendaryrealms.LegendaryGuild.Requirement;

import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;

public abstract class Requirement   {
    public final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    public final Lang lang = legendaryGuild.getFileManager().getLang();

    public abstract String getSymbol();

    public abstract boolean canPass(Player p, String str);
    public abstract void deal(Player p,String str);

    public  <T> T getFromArray(String[] str,int pos,T def){
        if (str != null && str.length > pos) {
            return (T)str[pos];
        }
        return def;
    }
}
