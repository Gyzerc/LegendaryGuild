package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MembersLoader extends MenuLoader {
    private Material mm_icon;
    private String mm_display;
    private int mm_data;
    private int mm_model;
    private List<String> mm_lore;
    public MembersLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "Members.yml");
    }

    @Override
    protected void readDefault() {
        mm_icon = getMaterial(getValue("playeritem.material","STONE"));
        mm_data = getValue("playeritem.data",0);
        mm_model = getValue("playeritem.model",0);
        mm_display = legendaryGuild.color(getValue("playeritem.display","%player%"));
        mm_lore = legendaryGuild.color(getValue("playeritem.lore",new ArrayList<>()));
    }

    public Material getMm_icon() {
        return mm_icon;
    }

    public String getMm_display() {
        return mm_display;
    }

    public int getMm_data() {
        return mm_data;
    }

    public int getMm_model() {
        return mm_model;
    }

    public List<String> getMm_lore() {
        return mm_lore;
    }
}
