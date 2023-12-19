package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuildListLoader extends MenuLoader {

    private Material guild_icon;
    private int guild_data;
    private int guild_model;
    private String guild_display;
    private List<String> guild_lore;


    public GuildListLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "GuildList.yml");
    }


    @Override
    protected void readDefault() {
        guild_icon = getMaterial(getValue("guild.material","STONE"));
        guild_display = legendaryGuild.color(getValue("guild.display",""));
        guild_data = getValue("guild.data",0);
        guild_model = getValue("guild.model",0);
        guild_lore = legendaryGuild.color(getValue("guild.lore",new ArrayList<>()));
        return;
    }

    public Material getGuild_icon() {
        return guild_icon;
    }

    public int getGuild_data() {
        return guild_data;
    }

    public int getGuild_model() {
        return guild_model;
    }

    public String getGuild_display() {
        return guild_display;
    }

    public List<String> getGuild_lore() {
        return guild_lore;
    }
}
