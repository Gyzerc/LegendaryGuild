package com.legendaryrealms.LegendaryGuild.Hook;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public abstract class Hook {
    public LegendaryGuild legendaryGuild;
    public Hook(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
    }

    public boolean enable;
    public boolean isEnable(){
        return enable;
    }
    public abstract boolean getHook();
}
