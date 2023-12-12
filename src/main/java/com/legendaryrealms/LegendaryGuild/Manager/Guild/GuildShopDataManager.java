package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.util.Calendar;
import java.util.logging.Level;

public class GuildShopDataManager {

    private GuildShopData data;
    private LegendaryGuild legendaryGuild;

    public GuildShopDataManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.data = legendaryGuild.getDataBase().getGuildShopData();
    }

    public GuildShopData getData(){
        if (data != null){
            return data;
        }
        data = legendaryGuild.getDataBase().getGuildShopData();
        return data;
    }

    public void updateData(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveGuildShopData(data);
            }
        });

    }

    public void saveData(){
        legendaryGuild.getDataBase().saveGuildShopData(data);
    }

    public void reloadData() {
        data = legendaryGuild.getDataBase().getGuildShopData();
    }

    public void checkDate(){
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DATE);
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int thisMonth = calendar.get(Calendar.MONTH);
        if (data.getLast_date() != today) {
            data.setLast_date(today);
            saveData();
            legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Day.name());
            legendaryGuild.info("新的一天到来了,公会日常限购数据刷新.",Level.INFO);
        }
        if (data.getLast_week() != thisWeek) {
            data.setLast_week(thisWeek);
            saveData();
            legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Week.name());
            legendaryGuild.info("新的一周到来了,公会每周限购数据刷新.",Level.INFO);
        }
        if (data.getLast_month() != thisMonth) {
            data.setLast_month(thisMonth);
            saveData();
            legendaryGuild.getDataBase().deleteGuildShopData(ShopType.Month.name());
            legendaryGuild.info("新的月份到来了,公会每月限购数据刷新.",Level.INFO);
        }
    }
}
