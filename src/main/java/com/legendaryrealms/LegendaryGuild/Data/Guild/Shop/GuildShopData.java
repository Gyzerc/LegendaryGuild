package com.legendaryrealms.LegendaryGuild.Data.Guild.Shop;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ItemBuyData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuildShopData {
    private HashMap<String, ItemBuyData> data;

    public GuildShopData(HashMap<String, ItemBuyData> data, int last_date, int last_week, int last_month) {
        this.data = data;
        this.last_date = last_date;
        this.last_week = last_week;
        this.last_month = last_month;
    }

    public void clearPlayerData(String player,ShopType type) {
        HashMap<String,ItemBuyData> map = new HashMap<>(data);
        for (Map.Entry<String,ItemBuyData> entry : map.entrySet()) {
            String id = entry.getKey();
            ItemBuyData buyData= entry.getValue();
            buyData.clear(player,type);
            map.put(id,buyData);
        }
    }
    public int getLast_date() {
        return last_date;
    }

    public int getLast_week() {
        return last_week;
    }

    public int getLast_month() {
        return last_month;
    }

    public void setLast_date(int last_date) {
        this.last_date = last_date;
    }

    public void setLast_week(int last_week) {
        this.last_week = last_week;
    }

    public void setLast_month(int last_month) {
        this.last_month = last_month;
    }

    private int last_date;
    private int last_week;
    private int last_month;
    public Optional<ItemBuyData> getData(String shopItemId){
        if (data.containsKey(shopItemId)){
            return Optional.of(data.get(shopItemId));
        }
        ItemBuyData itemBuyData = new ItemBuyData(shopItemId,new HashMap<>());
        data.put(shopItemId,itemBuyData);
        return Optional.of(itemBuyData);
    }

    public void setData(String itemId,ItemBuyData data) {
        this.data.put(itemId,data);
    }

    public void removeData(ShopType type) {
        HashMap<String,ItemBuyData> map = new HashMap<>();
        for (Map.Entry<String,ItemBuyData> entry:data.entrySet()) {
            String id = entry.getKey();
            ItemBuyData buyData = entry.getValue();
            buyData.clear(type);
            map.put(id,buyData);
        }
        this.data = map;
    }
    public String toString(ShopType type){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, ItemBuyData> entry:data.entrySet()){
            builder.append(entry.getValue().toString(type));
        }
        return builder.toString();
    }

    public static GuildShopData getData(int day,int week,int month,String... args){
        GuildShopData data = new GuildShopData(new HashMap<>(),day,week,month);
        if ( args != null ) {
            ShopType[] types = new ShopType[]{ShopType.ONCE, ShopType.DAY, ShopType.WEEK, ShopType.MONTH};
            for (int in = 0; in < types.length; in++) {
                String arg = args[in];
                if (arg != null) {
                    ShopType type = types[in];
                    for (String buyAmountStr : arg.split("⁞")) {
                        if (buyAmountStr != null && !buyAmountStr.isEmpty()) {
                            String itemId = buyAmountStr.split("⁝")[0];
                            String playerStr = buyAmountStr.split("⁝")[1];
                            for (String playerArg : playerStr.split(";")) {
                                if (playerArg != null && !playerArg.isEmpty()) {
                                    String player = playerArg.split(":")[0];
                                    int amount = Integer.parseInt(playerArg.split(":")[1]);
                                    ItemBuyData itemBuyData = data.getData(itemId).get();
                                    itemBuyData.setBuyAmount(player, type, amount);
                                    data.setData(itemId, itemBuyData);
                                }
                            }
                        }
                    }
                }
            }
        }
        return data;
    }

    public void updata(){
        LegendaryGuild.getInstance().getGuildShopDataManager().updateData(this);
    }
}
