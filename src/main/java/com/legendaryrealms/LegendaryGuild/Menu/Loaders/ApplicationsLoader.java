package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsLoader extends MenuLoader {
    private Material app_icon;
    private int app_data;
    private int app_model;
    private String app_display;
    private List<String> app_lore;

    public ApplicationsLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "Applications.yml");
    }

    @Override
    protected void readDefault() {
        app_icon = getMaterial(getValue("playeritem.material","STONE"));
        app_data = getValue("playeritem.data",0);
        app_model = getValue("playeritem.model",0);
        app_display = legendaryGuild.color(getValue("playeritem.display","%player%"));
        app_lore = legendaryGuild.color(getValue("playeritem.lore",new ArrayList<>()));
    }

    public Material getApp_icon() {
        return app_icon;
    }

    public int getApp_data() {
        return app_data;
    }

    public int getApp_model() {
        return app_model;
    }

    public String getApp_display() {
        return app_display;
    }

    public List<String> getApp_lore() {
        return app_lore;
    }
}
