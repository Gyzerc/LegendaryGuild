package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class StoresLoader extends MenuLoader {
    private Material store_icon_unlock;
    private String store_display_unlock;
    private int store_data_unlock;
    private int store_model_unlock;
    private List<String> store_lore_unlock;
    private Material store_icon_locked;
    private String store_display_locked;
    private int store_data_locked;
    private int store_model_locked;
    private List<String> store_lore_locked;

    public StoresLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "Stores.yml");
    }


    @Override
    protected void readDefault() {
        store_icon_unlock = getMaterial(getValue("store_item_unlock.material", "CHEST"));
        store_data_unlock = getValue("store_item_unlock.data", 0);
        store_model_unlock = getValue("store_item_unlock.model", 0);
        store_display_unlock = legendaryGuild.color(getValue("store_item_unlock.display", "&f%id% 号仓库"));
        store_lore_unlock = legendaryGuild.color(getValue("store_item_unlock.lore", new ArrayList<>()));

        store_icon_locked = getMaterial(getValue("store_item_locked.material", "CHEST"));
        store_data_locked = getValue("store_item_locked.data", 0);
        store_model_locked = getValue("store_item_locked.model", 0);
        store_display_locked = legendaryGuild.color(getValue("store_item_locked.display", "&f%id% 号仓库"));
        store_lore_locked = legendaryGuild.color(getValue("store_item_locked.lore", new ArrayList<>()));
    }

    public Material getStore_icon_unlock() {
        return store_icon_unlock;
    }

    public String getStore_display_unlock() {
        return store_display_unlock;
    }

    public int getStore_data_unlock() {
        return store_data_unlock;
    }

    public int getStore_model_unlock() {
        return store_model_unlock;
    }

    public List<String> getStore_lore_unlock() {
        return store_lore_unlock;
    }

    public Material getStore_icon_locked() {
        return store_icon_locked;
    }

    public String getStore_display_locked() {
        return store_display_locked;
    }

    public int getStore_data_locked() {
        return store_data_locked;
    }

    public int getStore_model_locked() {
        return store_model_locked;
    }

    public List<String> getStore_lore_locked() {
        return store_lore_locked;
    }
}
