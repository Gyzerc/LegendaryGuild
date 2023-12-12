package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class TributesFile extends FileProvider{
    public TributesFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config", "Contents/config/", "Tributes.yml");
    }

    @Override
    protected void readDefault() {

    }
}
