package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class TeamShopFile extends FileProvider{
    public TeamShopFile(LegendaryGuild legendaryGuild ) {
        super(legendaryGuild,"./plugins/LegendaryGuild/Contents/config","Contents/config/","TeamShop.yml");
    }

    @Override
    protected void readDefault() {

    }
}
