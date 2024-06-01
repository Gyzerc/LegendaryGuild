package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public void updateData(GuildShopData data){
        this.data = data;
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveGuildShopData(data);
                Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
                if (p != null) {
                    new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                            .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_GUILD_SHOP, "null"))
                            .setReciver("ALL")
                            .sendPluginMessage(p);
                }
            }
        });

    }

    public void reloadData() {
        data = legendaryGuild.getDataBase().getGuildShopData();
    }
}
