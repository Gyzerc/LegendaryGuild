package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

public class BuffLoader extends MenuLoader {
    public BuffLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus", "Contents/Menus/", "Buff.yml");
    }

    @Override
    protected void readDefault() {

    }
}
