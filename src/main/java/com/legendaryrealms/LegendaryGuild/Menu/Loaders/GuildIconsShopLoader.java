package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;

import java.util.Arrays;
import java.util.List;

public class GuildIconsShopLoader extends MenuLoader {
    public GuildIconsShopLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "GuildIconsShop.yml");
    }

    private String icon_display;
    private List<String> icon_lore;

    @Override
    protected void readDefault() {
        icon_display = legendaryGuild.color(getValue("icon_item.display","%&1图标 &f- %icon%%"));
        icon_lore = legendaryGuild.color(getValue("icon_item.lore", Arrays.asList("%description%"," ","&f[ %placeholder% &f]")));
    }

    public String getIcon_display() {
        return icon_display;
    }

    public List<String> getIcon_lore() {
        return icon_lore;
    }
}
