package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class WaterPotsFile extends FileProvider{
    public WaterPotsFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild,"./plugins/LegendaryGuild/Contents/config","Contents/config/","WaterPots.yml");
    }

    @Override
    protected void readDefault() {

    }
}
