package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildIcon;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GuildIconsManager {
    private LinkedHashMap<String, GuildIcon> cache;
    private LegendaryGuild legendaryGuild;

    public GuildIconsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new LinkedHashMap<>();
        readIcons();
    }

    private void readIcons() {
       ConfigurationSection section = legendaryGuild.getFileManager().getGuildIconsFile().getSection("icons").orElse(null);
       int a = 0;
       if (section != null){
            for (String iconId : section.getKeys(false)){
                Material material = Material.getMaterial(section.getString(iconId+".material","APPLE")) != null ? Material.getMaterial(section.getString(iconId+".material","APPLE")) : Material.APPLE;
                String display = legendaryGuild.color(section.getString(iconId+".display","&f图标"));
                int data = section.getInt(iconId+".data",0);
                int model = section.getInt(iconId+".model",0);
                List<String> description = legendaryGuild.color(section.getStringList(iconId+".description"));
                List<String> requirements = legendaryGuild.color(section.getStringList(iconId+".requirements"));

                ItemStack i = new ItemStack(material,1,(short) data);
                ItemMeta id = i.getItemMeta();
                if (legendaryGuild.version_high){
                    id.setCustomModelData(model);
                }
                i.setItemMeta(id);
                cache.put(iconId,new GuildIcon(iconId,display,i,description,requirements));
                a ++ ;
            }
        }
       legendaryGuild.info("加载 "+a+" 个公会图标.", Level.INFO);
    }

    public Optional<GuildIcon> getIcon(String iconId){
        if (iconId == null) {return Optional.empty();}
        return cache.containsKey(iconId) ? Optional.of(cache.get(iconId)) : Optional.empty();
    }

    public LinkedList<GuildIcon> getIcons(){
        return new LinkedList<>(cache.values());
    }
}
