package com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopItem {
    private String id;
    private ItemStack display;
    private ShopType type;
    private int limitAmount;
    private List<String> requirements;
    private List<String> runs;

    public ShopItem(String id, ItemStack display, ShopType type, int limitAmount, List<String> requirements, List<String> runs) {
        this.id = id;
        this.display = display;
        this.type = type;
        this.limitAmount = limitAmount;
        this.requirements = requirements;
        this.runs = runs;
    }

    public String getId() {
        return id;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public ShopType getType() {
        return type;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public List<String> getRuns() {
        return runs;
    }

}
