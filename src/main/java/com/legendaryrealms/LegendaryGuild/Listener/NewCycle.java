package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Listener.Custom.NewCycleEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class NewCycle implements Listener {
    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    @EventHandler
    public void newCycle(NewCycleEvent e){

        int id = e.getId();
        String[] name = {"last_date","last_week","last_month"};
        //更新数据
        legendaryGuild.getDataBase().saveSystemData(name[id],e.getValue()+"");


        GuildShopData guildShopData = legendaryGuild.getGuildShopDataManager().getData();
        switch (id){
            case 0:
                //刷新全体玩家数据
                updateDayilyData();

                //公会商店每日限购缓存刷新
                guildShopData.setLast_date(e.getValue());
                guildShopData.updata();
                legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Day.name());
                legendaryGuild.info("新的一天到来了,公会日常限购数据刷新.",Level.INFO);

                //公会活跃度检测
                legendaryGuild.getGuildActivityDataManager().checkCycle();
                break;
            case 1:
                //公会商店每周限购缓存刷新
                guildShopData.setLast_week(e.getValue());
                guildShopData.updata();
                legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Week.name());
                legendaryGuild.info("新的一周到来了,公会每周限购数据刷新.",Level.INFO);

                break;
            case 2:
                //公会商店每月限购缓存刷新
                guildShopData.setLast_month(e.getValue());
                guildShopData.updata();
                legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Month.name());
                legendaryGuild.info("新的月份到来了,公会每月限购数据刷新.", Level.INFO);

                break;
        }
    }


    private void updateDayilyData(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach( p -> {
                    String name = p.getName();
                    User user = UserAPI.getUser(name);
                    user.setWish(false);
                    WaterDataStore waterDataStore = user.getWaterDataStore();
                    waterDataStore.clearWaterDay();
                    user.setWaterDataStore(waterDataStore);
                    legendaryGuild.getUsersManager().updateUser(user,true);
                });

                List<String> online = Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
                for (String userName : legendaryGuild.getDataBase().getUsers().stream().filter( n -> {
                    if (online.contains( n)){
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList())) {
                    User user = UserAPI.getUser(userName);
                    user.setWish(false);
                    WaterDataStore waterDataStore = user.getWaterDataStore();
                    waterDataStore.clearWaterDay();
                    user.setWaterDataStore(waterDataStore);
                    legendaryGuild.getUsersManager().updateUser(user,true);
                }
            }
        });
    }
}
