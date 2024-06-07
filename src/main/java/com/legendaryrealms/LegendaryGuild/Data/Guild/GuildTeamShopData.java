package com.legendaryrealms.LegendaryGuild.Data.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Data.Others.TeamShopItem;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GuildTeamShopData {
    private String guild;
    private String todayShopId;
    private double currentPrice;
    private HashMap<String,Double> bargains;
    private HashMap<String,Integer> buy;

    public GuildTeamShopData(String guild, String todayShopId, double currentPrice, HashMap<String, Double> bargains, HashMap<String, Integer> buy) {
        this.guild = guild;
        this.todayShopId = todayShopId;
        this.currentPrice = currentPrice;
        this.bargains = bargains;
        this.buy = buy;
    }

    public double getBargainPrice(String playerName) {
        return bargains.getOrDefault(playerName,0.0);
    }
    public String getGuild() {
        return guild;
    }

    public String getTodayShopId() {
        return todayShopId;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public boolean hasBargain(String playerName) {
        return bargains.containsKey(playerName);
    }

    public int getBuyAmount(String playerName) {
        return buy.getOrDefault(playerName,0);
    }

    public HashMap<String, Double> getBargains() {
        return bargains;
    }

    public HashMap<String, Integer> getBuy() {
        return buy;
    }
    public void update(boolean removeIfCached) {
        LegendaryGuild.getInstance().getGuildTeamShopDataManager().update(this,removeIfCached);

        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_TEAMSHOPDATA, guild))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
        }
    }
    public void clearBuys() {
        buy.clear();
    }
    public void resetPlayerBuys(String playerName,int amount) {
        buy.put(playerName,amount);
    }

    public void randomShop() {
        List<String> ids = LegendaryGuild.getInstance().getTeamShopManager().getShopItems();
        if (ids.size() > 0) {
            int roll = new Random().nextInt(ids.size());
            this.todayShopId = ids.get(roll);
            TeamShopItem item = LegendaryGuild.getInstance().getTeamShopManager().getShopItem(todayShopId);
            this.buy = new HashMap<>();
            this.bargains = new HashMap<>();
            this.currentPrice = item.getPRICE_BASE();
            update(false);
        }
    }

    public void bargain(Guild guild,String player) {
        if (!bargains.containsKey(player)) {

            TeamShopItem shopItem = LegendaryGuild.getInstance().getTeamShopManager().getShopItem(todayShopId);

            double base = shopItem.getPRICE_BASE();
            double min = shopItem.getPRICE_MIN();
            int member = guild.getMembers().size();
            int max_member = guild.getMaxMembers();
            double canBargained = base > min ? base - min : 0;
            double bargain = 0.0;


            if (currentPrice > min) {
                switch (LegendaryGuild.getInstance().getFileManager().getConfig().bargainMode) {
                    case RANDOM: {
                        //当可砍价
                        if (canBargained > 0) {

                            bargain = new Random().nextInt((int) canBargained);
                            //当随机后的值过大，进行矫正
                            if (currentPrice - bargain < min) {
                                bargain = (currentPrice - min);
                            }
                        }
                        break;
                    }
                    case BASE_ON_MAXMEMBER: {
                        //获取每次砍价的最大价格值
                        double single = canBargained / max_member;
                        bargain = new Random().nextInt((int) canBargained);

                        //当随机后的值过大，进行矫正
                        if (currentPrice - bargain < min) {
                            bargain = (currentPrice - min);
                        }
                        break;
                    }
                    case BASE_ON_MEMBER: {
                        double single = canBargained / member;

                        bargain = new Random().nextInt((int) canBargained);

                        //当随机后的值过大，进行矫正
                        if (currentPrice - bargain < min) {
                            bargain = (currentPrice - min);
                        }
                        break;
                    }
                }
            }
            currentPrice -= bargain;
            bargains.put(player,bargain);
            update(false);

        }
    }

    public void addBuyAmount(String name, int i) {
        int amount = getBuyAmount(name);
        buy.put(name,amount + i);
        update(false);
    }


    public enum BargainMode {
        BASE_ON_MAXMEMBER,
        BASE_ON_MEMBER,
        RANDOM;
    }
}
