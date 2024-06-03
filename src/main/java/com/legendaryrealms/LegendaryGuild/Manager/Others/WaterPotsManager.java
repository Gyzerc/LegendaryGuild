package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.Data.Others.WaterPot;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class WaterPotsManager {
    private LegendaryGuild legendaryGuild;
    private HashMap<String, WaterPot> cache;

    public WaterPotsManager(LegendaryGuild legendaryGuild) {

        this.legendaryGuild = legendaryGuild;
        this.cache = new HashMap<>();
        initWaterPots();

    }

    public Optional<WaterPot> getWaterPot(String id) {
        return cache.containsKey(id) ? Optional.of(cache.get(id)) : Optional.empty();
    }
    public List<String> getPots() {
        return cache.keySet().stream().collect(Collectors.toList());
    }

    private void initWaterPots() {
        int a = 0;
        ConfigurationSection section = legendaryGuild.getFileManager().getWaterPotsFile().getSection("pots").orElse(null);
        if (section != null) {
           for (String id : section.getKeys(false)){
               String display = legendaryGuild.color(section.getString(id+".display","&f水壶"));
               List<String> requirements = section.getStringList(id+".requirements");
               List<String> runs = section.getStringList(id+".run");
               double addExp = section.getDouble(id+".addExp",1);
               double addPoints = section.getDouble(id+".addPoints",1);
               int limit = section.getInt(id+".day",-1);
               cache.put(id,new WaterPot(id,display, addExp,addPoints,limit, requirements,runs));
               a++;
           }
        }
        legendaryGuild.info("加载 "+a+ " 个神树水壶. & Load "+a+" Guild Tree Pots.", Level.INFO);
    }

}
