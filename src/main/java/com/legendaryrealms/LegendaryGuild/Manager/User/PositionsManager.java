package com.legendaryrealms.LegendaryGuild.Manager.User;

import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class PositionsManager {
    private LegendaryGuild legendaryGuild;
    private HashMap<String, Position> cache;
    private Position defaultPosition;
    private Position ownerPosition;
    public PositionsManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        cache = new HashMap<>();
    }
    public void addPostion(String id,Position position){
        cache.put(id,position);
    }

    public void setDefaultPosition(Position defaultPosition) {
        this.defaultPosition = defaultPosition;
        cache.put(defaultPosition.getId(),defaultPosition);
    }

    public void setOwnerPosition(Position ownerPosition) {
        this.ownerPosition = ownerPosition;
        cache.put(ownerPosition.getId(),ownerPosition);
    }

    public Position getDefaultPosition() {
        return defaultPosition;
    }

    public Position getOwnerPosition() {
        return ownerPosition;
    }

    public void CanEnable(){
        if (defaultPosition == null || ownerPosition == null){
            legendaryGuild.info("请检查公会职位配置. & Please check the configuration!", Level.SEVERE);
            Bukkit.getPluginManager().disablePlugin(legendaryGuild);

        }
        legendaryGuild.info("加载 "+cache.size() +" 个公会职位 & Load "+cache.size()+" Guild Positions.",Level.INFO);
    }

    public List<String> getPositionIds(){
        List<String> ids=new ArrayList<>();
        cache.forEach((id,po) -> {
            ids.add(id);
        });
        return ids;
    }
    public Optional<Position> getPosition(String id){
        return cache.containsKey(id) ? Optional.of(cache.get(id)) : Optional.empty();
    }
}
