package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

public class GuildMenuLoader extends MenuLoader {
    public GuildMenuLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus", "Contents/Menus/", "GuildMenu.yml");
    }

    @Override
    protected void readDefault() {

    }
}
