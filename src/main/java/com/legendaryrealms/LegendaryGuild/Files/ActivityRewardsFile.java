package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class ActivityRewardsFile extends FileProvider{
    public ActivityRewardsFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config", "Contents/config/", "ActivityRewards.yml");
    }

    @Override
    protected void readDefault() {

    }
}
