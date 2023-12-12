package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class BuffFile extends FileProvider{
    public BuffFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config","Contents/config/", "Buff.yml");
    }

    public boolean getEnable(){
        return getValue("enable",false);
    }

    @Override
    protected void readDefault() {

    }
}
