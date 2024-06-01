package com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item;

import java.util.HashMap;
import java.util.Map;

public class BuyAmount {
    private String player;
    private HashMap<ShopType, Integer> amounts;

    public BuyAmount(String player, HashMap<ShopType, Integer> amounts) {
        this.player = player;
        this.amounts = amounts;
    }

    public BuyAmount clear(ShopType type) {
        amounts.remove(type);
        return this;
    }
    public int getAmount(ShopType type) {
        return amounts.containsKey(type) ? amounts.get(type) : 0;
    }

    public void addAmount(ShopType type, int amount) {
        int has = getAmount(type);
        amounts.put(type, (has + amount));
    }

    public void setAmount(ShopType type, int amount) {
        amounts.put(type, amount);
    }

    public void setAmounts(HashMap<ShopType, Integer> amounts) {
        this.amounts = amounts;
    }

    public String getPlayer() {
        return player;
    }


    // item1⁝ (p1:0;p2:0⁞)


    public String toString(ShopType type){
        StringBuilder builder = new StringBuilder();
            builder.append(player).append(":")
                    .append(getAmount(type)).append(";");

        return builder.toString();
    }
}
