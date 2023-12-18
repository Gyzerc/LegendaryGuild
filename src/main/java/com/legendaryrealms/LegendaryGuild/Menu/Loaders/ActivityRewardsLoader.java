package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

public class ActivityRewardsLoader extends MenuLoader {


    public ActivityRewardsLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus", "Contents/Menus/", "ActivityRewards.yml");
    }

    @Override
    protected void readDefault() {

    }
}
