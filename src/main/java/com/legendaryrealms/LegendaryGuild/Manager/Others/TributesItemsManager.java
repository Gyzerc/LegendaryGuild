package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.Data.Others.TributeItem;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class TributesItemsManager {
    private HashMap<String, TributeItem> cache;
    private LegendaryGuild legendaryGuild;

    public TributesItemsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new HashMap<>();
        readTributes();
    }

    public Optional<TributeItem> getTributesItem(String id){
        return cache.containsKey(id) ? Optional.of(cache.get(id)) : Optional.empty();
    }

    public List<TributeItem> getTributesItems(){
        return new ArrayList<>(cache.values());
    }

    private void readTributes() {
        ConfigurationSection section = legendaryGuild.getFileManager().getTributesFile().getSection("tributes").orElse(null);
        int a = 0;
        if (section != null){
            for (String id : section.getKeys(false)){

                Material material = section.getString(id+".material") != null ? Material.getMaterial(section.getString(id+".material").toUpperCase() ): null;
                int data = section.getInt(id+".data",0);
                String display = legendaryGuild.color(section.getString(id+".display",null));

                double points = section.getDouble(id+".points",5);
                double exp = section.getDouble(id+".exp",5);

                boolean broad = section.getBoolean(id+".broad",false);
                String message = legendaryGuild.color(section.getString(id+".broad_message",""));
                cache.put(id,new TributeItem(id,material,data,display,points,exp,broad,message));
                a++;
            }
        }
        legendaryGuild.info("加载 "+a+" 个贡品. & Load "+a+" Guild Tribute Items.", Level.INFO);
    }

    public Optional<TributeItem> getTributeItem(ItemStack i){
        for (TributeItem tributeItem : getTributesItems()){
            if (tributeItem.getMaterial() != null){
                if (!i.getType().equals(tributeItem.getMaterial()) || i.getDurability() != (short)tributeItem.getData()){
                    continue;
                }
            }
            if (tributeItem.getDisplay() != null){
                String itemName = i.getType().name();
                if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()){
                    itemName = i.getItemMeta().getDisplayName();
                }
                if (!itemName.equals(tributeItem.getDisplay())){
                    continue;
                }
            }
            return Optional.of(tributeItem);
        }
        return Optional.empty();
    }
}
