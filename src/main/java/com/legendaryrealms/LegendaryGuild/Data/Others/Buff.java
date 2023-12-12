package com.legendaryrealms.LegendaryGuild.Data.Others;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Buff {
    private String id;
    private String display;
    private int max;
    private IntStore<List<String>> requirements;
    private IntStore<ItemStack> preview;
    private IntStore<List<String>> attr;

    public Buff(String id, String display, int max,IntStore<ItemStack> preview, IntStore<List<String>> requirements, IntStore<List<String>> attr) {
        this.id = id;
        this.display = display;
        this.max = max;
        this.requirements = requirements;
        this.preview = preview;
        this.attr = attr;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public int getMax() {
        return max;
    }
    public ItemStack getPreview(int level){
        return preview.getValue(level,null);
    }

    public List<String> getRequirements(int level){
        return requirements.getValue(level,new ArrayList<>());
    }

    public List<String> getAttr(int level){
        return attr.getValue(level,new ArrayList<>());
    }
}
