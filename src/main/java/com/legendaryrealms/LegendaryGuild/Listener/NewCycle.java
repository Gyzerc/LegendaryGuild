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
                updateDailyData();

                //公会团购数据刷新
                legendaryGuild.getDataBase().clearGuildTeamShopData(null);

                //公会商店每日限购缓存刷新
                guildShopData.setLast_date(e.getValue());
                guildShopData.removeData(ShopType.DAY);
                guildShopData.updata();
                legendaryGuild.info("新的一天到来了,公会日常限购数据刷新.",Level.INFO);
                legendaryGuild.info("A new day has arrived, and the guild's daily purchase restriction data has been refreshed.",Level.INFO);

                //公会活跃度检测
                legendaryGuild.getGuildActivityDataManager().checkCycle();
                break;
            case 1:
                //公会商店每周限购缓存刷新
                guildShopData.setLast_week(e.getValue());
                guildShopData.removeData(ShopType.WEEK);
                guildShopData.updata();
                legendaryGuild.info("新的一周到来了,公会每周限购数据刷新.",Level.INFO);
                legendaryGuild.info("A new week has arrived, and the guild's weekly purchase restriction data is refreshed.",Level.INFO);

                break;
            case 2:
                //公会商店每月限购缓存刷新
                guildShopData.setLast_month(e.getValue());
                guildShopData.removeData(ShopType.MONTH);
                guildShopData.updata();
                legendaryGuild.info("新的月份到来了,公会每月限购数据刷新.", Level.INFO);
                legendaryGuild.info("A new month has arrived, and the guild's weekly purchase restriction data is refreshed.",Level.INFO);
                break;
        }
    }


    private void updateDailyData(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                for (String userName : legendaryGuild.getDataBase().getUsers().stream().collect(Collectors.toList())) {
                    User user = UserAPI.getUser(userName);
                    user.setWish(false);
                    WaterDataStore waterDataStore = user.getWaterDataStore();
                    waterDataStore.clearWaterDay();
                    user.setWaterDataStore(waterDataStore);
                    user.update(true);
                    legendaryGuild.getUsersManager().updateUser(user,true);
                }
            }
        });
    }
}
