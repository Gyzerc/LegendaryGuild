package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

public class PositionsLoader extends MenuLoader {
    public PositionsLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus", "Contents/Menus/", "Positions.yml");
    }

    @Override
    protected void readDefault() {

    }
}
