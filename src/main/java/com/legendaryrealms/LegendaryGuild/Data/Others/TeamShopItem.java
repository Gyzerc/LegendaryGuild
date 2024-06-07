package com.legendaryrealms.LegendaryGuild.Data.Others;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TeamShopItem {
    private String id;
    private double PRICE_BASE;
    private double PRICE_MIN;
    private int limit;
    private TeamShopItemCurrency currency;
    private ItemStack preview;
    private List<String> run;
    private String display;

    public TeamShopItem(String id,String display, double PRICE_BASE, double PRICE_MIN, int limit, TeamShopItemCurrency currency, ItemStack preview, List<String> run) {
        this.id = id;
        this.display = display;
        this.PRICE_BASE = PRICE_BASE;
        this.PRICE_MIN = PRICE_MIN;
        this.limit = limit;
        this.currency = currency;
        this.preview = preview;
        this.run = run;
    }

    public String getId() {
        return id;
    }

    public double getPRICE_BASE() {
        return PRICE_BASE;
    }

    public double getPRICE_MIN() {
        return PRICE_MIN;
    }

    public int getLimit() {
        return limit;
    }

    public TeamShopItemCurrency getCurrency() {
        return currency;
    }

    public ItemStack getPreview() {
        return preview;
    }

    public List<String> getRun() {
        return run;
    }

    public String getDisplay() {
        return display;
    }


    public enum TeamShopItemCurrency {
        VAULT,
        PLAYERPOINTS,
        GUILD_POINTS;
    }
}
