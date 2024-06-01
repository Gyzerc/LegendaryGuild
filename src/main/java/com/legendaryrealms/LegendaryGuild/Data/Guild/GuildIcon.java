package com.legendaryrealms.LegendaryGuild.Data.Guild;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuildIcon {
    private String id;
    private String display;
    private Material material;
    private int data;
    private int model;
    private List<String> description;
    private List<String> requirements;

    public GuildIcon(String id, String display, Material material, int data, int model, List<String> description, List<String> requirements) {
        this.id = id;
        this.display = display;
        this.material = material;
        this.data = data;
        this.model = model;
        this.description = description;
        this.requirements = requirements;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public int getModel() {
        return model;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }


    public List<String> getDescription() {
        return description;
    }

    public List<String> getRequirements() {
        return requirements;
    }
}
