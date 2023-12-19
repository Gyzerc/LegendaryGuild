package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

public class GuildShopLoader extends MenuLoader {
    public GuildShopLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "GuildShop.yml");
    }

    @Override
    protected void readDefault() {

    }
}
