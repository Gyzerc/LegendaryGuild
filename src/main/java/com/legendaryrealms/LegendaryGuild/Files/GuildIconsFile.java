package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class GuildIconsFile extends FileProvider{
    public GuildIconsFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config","Contents/config/", "GuildIcons.yml");
    }

    @Override
    protected void readDefault() {

    }
}
