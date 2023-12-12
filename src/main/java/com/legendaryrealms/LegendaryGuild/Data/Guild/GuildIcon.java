package com.legendaryrealms.LegendaryGuild.Data.Guild;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuildIcon {
    private String id;
    private String display;
    private ItemStack icon;
    private List<String> description;
    private List<String> requirements;

    public GuildIcon(String id,String display, ItemStack icon, List<String> description, List<String> requirements) {
        this.id = id;
        this.display = display;
        this.icon = icon;
        this.description = description;
        this.requirements = requirements;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<String> getRequirements() {
        return requirements;
    }
}
