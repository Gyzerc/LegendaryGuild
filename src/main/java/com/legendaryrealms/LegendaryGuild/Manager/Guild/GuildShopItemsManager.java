package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopItem;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GuildShopItemsManager {

    private LinkedHashMap<String, ShopItem> cache;
    private LegendaryGuild legendaryGuild;

    public GuildShopItemsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new LinkedHashMap<>();
        readItems();
    }

    private void readItems() {
        int a = 0;
        ConfigurationSection section = legendaryGuild.getFileManager().getGuildShopFile().getSection("items").orElse(null);
        if (section != null){
            for (String shopId : section.getKeys(false)){
                ItemStack i = ItemUtils.readItem(section,shopId);
                List<String> requirements = legendaryGuild.color(section.getStringList(shopId+".requirements"));
                List<String> runs = legendaryGuild.color(section.getStringList(shopId+".run"));
                ShopType type = getType(section.getString(shopId+".buy_limit.type","Unlimited"));
                int limit = section.getInt(shopId+".buy_limit.amount",-1);
                cache.put(shopId,new ShopItem(shopId,i,type,limit,requirements,runs));
                a++;
            }
        }
        legendaryGuild.info("加载 "+a +" 个公会商店商品.  & Load "+a +" Guild Shop items.", Level.INFO);

    }

    private ShopType getType(String type){
        try {
            return ShopType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            legendaryGuild.info("商品限购类型配置出错，已默认为无限购商品 -> "+type,Level.SEVERE);
            legendaryGuild.info("Product purchase restriction type configuration error, defaulted to unlimited purchase product",Level.SEVERE);
            return ShopType.UNLIMITED;
        }
    }

    public LinkedList<ShopItem> getItems(){
        return new LinkedList<>(cache.values());
    }
}
