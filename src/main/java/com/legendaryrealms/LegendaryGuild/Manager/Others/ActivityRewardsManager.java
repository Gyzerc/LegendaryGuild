package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.Data.Others.ActivityReward;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class ActivityRewardsManager {
    private HashMap<String, ActivityReward> cache;
    private LegendaryGuild legendaryGuild;

    public ActivityRewardsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new HashMap<>();
        loadRewards();
    }

    private void loadRewards() {
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getFileManager().getActivityRewardsFile().getSection("rewards").ifPresent(configurationSection -> {
                    int a = 0;
                    for (String id : configurationSection.getKeys(false)){
                        String display = legendaryGuild.color(configurationSection.getString(id+".display",""));
                        double points = configurationSection.getDouble(id+".activity",1000);
                        List<String> run = configurationSection.getStringList(id+".run");
                        cache.put(id,new ActivityReward(id,display,points,run));
                        a++;
                    }
                    legendaryGuild.info("加载 "+a+" 个活跃度奖励. & Load "+a+" Activity rewards.", Level.INFO);
                });
            }
        });

    }

    public Optional<ActivityReward> getReward(String id){
        return cache.containsKey(id) ? Optional.of(cache.get(id)) : Optional.empty();
    }

}
