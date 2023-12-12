package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class GuildsManager {

    private HashMap<String, Guild> cache;
    private LegendaryGuild legendaryGuild;
    private Lang lang;
    public GuildsManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        this.lang = legendaryGuild.getFileManager().getLang();
        cache = new HashMap<>();
    }

    public boolean isExists(String guildName){
        return getGuilds().contains(guildName);
    }

    public void reloadGuildIfCached(String guild){
        cache.put(guild,legendaryGuild.getDataBase().getGuild(guild).get());
    }

    public Guild createGuild(String guild, User user){
        Player p = Bukkit.getPlayerExact(user.getPlayer());
        if (p == null){
            return null;
        }
        String owner = user.getPlayer();
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LinkedList<String> members = new LinkedList<>();
        members.add(owner);
        Guild guildData = new Guild(guild,owner,"",df.format(System.currentTimeMillis()),0.0,0.0,0.0,0,0,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new StringStore(),new ArrayList<>(), members,new LinkedList<>(),new Guild.GuildHomeLocation("null",LegendaryGuild.getInstance().SERVER,0.0,0.0,0.0));
        cache.put(guild,guildData);
        //更新公会数据库
        updateGuild(guildData,false);

        //设置基础数据
        user.setGuild(guild);
        user.setPosition(legendaryGuild.getPositionsManager().getOwnerPosition().getId());
        user.setPoints(0.0,false);
        //更新数据库数据以及通知其他子服务器更新数据
        user.update();

        //通知其他子服务器更新公会数据
        //....
        new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_GUILD, guild))
                .setReciver("ALL")
                .sendPluginMessage(p);
        return guildData;

    }

    public Guild getGuild(String guild){
        Guild data = cache.get(guild) ;
        if (data == null) {
            data = legendaryGuild.getDataBase().getGuild(guild).orElse(null);
            if (data != null){
                cache.put(guild,data);
            }
        }
        return data;
    }


    public void updateGuild(Guild guild,boolean removeCache){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                legendaryGuild.getDataBase().saveGuild(guild);
                if (removeCache){
                    cache.remove(guild.getGuild());
                }
            }
        });

    }

    public List<String> getGuilds(){
        return legendaryGuild.getDataBase().getGuilds();
    }

    public void loadGuilds(){
                getGuilds().forEach(g -> {
                    Guild guild = legendaryGuild.getDataBase().getGuild(g).orElse(null);
                    if (guild != null) {
                        cache.put(g,guild);
                    }
                });
                legendaryGuild.info("载入 "+cache.size()+" 个公会.", Level.INFO);
    }
}
