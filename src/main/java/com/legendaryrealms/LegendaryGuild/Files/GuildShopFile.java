package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class GuildShopFile extends FileProvider{
    public GuildShopFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config","Contents/config/", "GuildShop.yml");
    }

    @Override
    protected void readDefault() {

    }
}
