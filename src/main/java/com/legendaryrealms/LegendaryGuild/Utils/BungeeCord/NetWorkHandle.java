package com.legendaryrealms.LegendaryGuild.Utils.BungeeCord;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Listener.MoveEvent;
import com.legendaryrealms.LegendaryGuild.Listener.PlayerJoin;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.StoresPanel;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class NetWorkHandle extends NetWork{


    public NetWorkHandle(LegendaryGuild legendaryGuild) {
        super(legendaryGuild);

    }



    @Override
    public void initNetwork() {
        if (legendaryGuild.getFileManager().getConfig().CROSS_SERVER){
            legendaryGuild.registerBungeecordChannel();
            legendaryGuild.info("成功启用跨服同步功能 & Enabled cross server synchronization function", Level.INFO);
            setEnable(true);
        }
    }

    @Override
    public void disable() {
        if (legendaryGuild.getFileManager().getConfig().CROSS_SERVER){
            legendaryGuild.unregisterBungeecordChannel();
            legendaryGuild.info("关闭跨服同步功能 & Closed cross server synchronization function", Level.INFO);
        }
    }

    @Override
    public void handle(NetWorkMessage.NetWorkType type, String value) {
        switch (type){
            case UPDATE_USER: {
                legendaryGuild.getUsersManager().reloadUserDataIfCached(value);
                break;
            }
            case UPDATE_GUILD: {
                legendaryGuild.getGuildsManager().removeGuildCache(value);
                break;
            }
            case REMOVE_GUILD: {
                legendaryGuild.getGuildsManager().removeGuildCache(value);
                break;
            }
            case UPDATE_STORE: {
                legendaryGuild.getStoresManager().reload(value);
                break;
            }
            case UPDATE_GUILD_SHOP:
                legendaryGuild.getGuildShopDataManager().reloadData();
                break;
            case UPDATE_REDPACKEY: {
                legendaryGuild.getRedPacketsManager().reloadRedPacket(value);
                break;
            }
            case UPDATE_GUILD_ACTIVITY_DATA:
                legendaryGuild.getGuildActivityDataManager().reloadGuildIfCached(value);
                break;
            case REFRESH_ACTIVITY:
                legendaryGuild.getGuildActivityDataManager().removeAll();
                break;
            case UPDATE_TEAMSHOPDATA:
                legendaryGuild.getGuildTeamShopDataManager().removeIfCached(value);
                break;
            case SCAN_PLAYER_IS_OPEN_STORE: {
                String player = value.split("/")[0];
                int id = Integer.parseInt(value.split("/")[1]);
                Player p = Bukkit.getPlayerExact(player);
                if (p != null){
                    if (p.getOpenInventory().getTopInventory().getHolder() instanceof StoresPanel.StoreContainer){
                        return;
                    }
                    User user = legendaryGuild.getUsersManager().getUser(player);
                    GuildStore store = legendaryGuild.getStoresManager().getStore(user.getGuild());
                    store.setUse(id,"null");
                    legendaryGuild.getStoresManager().update(store);
                    //通知去其他服务器
                    new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                            .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_STORE, user.getGuild()))
                            .setReciver("ALL")
                            .sendPluginMessage(p);
                }
                break;
            }
        }
    }
}
