package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.Data.Others.TeamShopItem;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TeamShopManager {
    private HashMap<String, TeamShopItem> caches;
    private LegendaryGuild legendaryGuild;

    public TeamShopManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        caches = new HashMap<>();
        loadItems();
    }

    private void loadItems() {
        legendaryGuild.getFileManager().getTeamShopFile().getSection("shops").ifPresent(section -> {
            for (String id : section.getKeys(false)) {
                String display = legendaryGuild.color(section.getString(id+".display","公会团购礼包"));
                double base = section.getDouble(id+".price.base");
                double min = section.getDouble(id+".price.min",0);
                int limit = section.getInt(id+".limit",-1);
                TeamShopItem.TeamShopItemCurrency currency = TeamShopItem.TeamShopItemCurrency.valueOf(section.getString(id+".currency","PLAYERPOINTS").toUpperCase());
                ItemStack preview = ItemUtils.readItem(section,id+".preview");
                List<String> run = section.getStringList(id+".run");
                caches.put(id,new TeamShopItem(id,display,base,min,limit,currency,preview,run));

            }
        });
        legendaryGuild.info("加载 "+caches.size() +" 个团购商品. & Load "+caches.size() +" team shop items.", Level.INFO);
    }

    public TeamShopItem getShopItem(String id) {
        return caches.get(id);
    }
    public List<String> getShopItems() {
        return caches.keySet().stream().collect(Collectors.toList());
    }

}
