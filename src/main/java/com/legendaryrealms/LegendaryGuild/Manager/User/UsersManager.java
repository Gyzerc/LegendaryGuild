package com.legendaryrealms.LegendaryGuild.Manager.User;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;

import java.util.HashMap;
import java.util.Optional;

public class UsersManager {
    private HashMap<String, User> cache;
    private LegendaryGuild legendaryGuild;
    public UsersManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        cache = new HashMap<>();
    }

    public User getUser(String player) {
        User user = cache.get(player);
        if (user == null) {
            user = legendaryGuild.getDataBase().getUser(player).orElse(
                    new User(player, legendaryGuild.getFileManager().getLang().default_guild, legendaryGuild.getFileManager().getLang().default_position, "", new WaterDataStore(new HashMap<>()), 0,false, false, 0, 0, User.PvpType.ALL)
            );
            cache.put(player,user);
        }
        return user;
    }


    public void updateUser(User user,boolean removeInCache){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveUser(user);
                if (removeInCache){
                    cache.remove(user.getPlayer());
                }
            }
        });

    }

    public void reloadUserDataIfCached(String target){
        cache.remove(target);
    }



}
