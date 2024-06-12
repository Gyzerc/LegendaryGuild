package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildListPanel;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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

    public void removeGuildCache(String guild){
        cache.remove(guild);
    }


    public List<Guild> getGuildsBy(GuildListPanel.Sort sort){

        List<Guild> guilds = legendaryGuild.getGuildsManager().getGuilds().stream().map(s -> {
            return legendaryGuild.getGuildsManager().getGuild(s);
        }).collect(Collectors.toList());

        if (sort.equals(GuildListPanel.Sort.DEFAULT)){
            return guilds;
        }

        Collections.sort(guilds, new Comparator<Guild>() {
            @Override
            public int compare(Guild o1, Guild o2) {
                switch (sort){
                    case LEVEL:
                        return (o1.getLevel() > o2.getLevel()) ? -1 : ((o1.getLevel() == o2.getLevel()) ? 0 : 1);
                    case MONEY:
                        return (o1.getMoney() > o2.getMoney()) ? -1 : ((o1.getMoney() == o2.getMoney()) ? 0 : 1);
                    case MEMBERS:
                        int a1 = o1.getMembers().size();
                        int a2 = o2.getMembers().size();
                        return (a1 > a2) ? -1 : ((a1 == a2) ? 0 : 1);
                    case ACTIVITY:
                        GuildActivityData data1 = legendaryGuild.getGuildActivityDataManager().getData(o1.getGuild());
                        GuildActivityData data2 = legendaryGuild.getGuildActivityDataManager().getData(o2.getGuild());
                        return (data1.getPoints() > data2.getPoints()) ? -1 : ((data1.getPoints() == data2.getPoints()) ? 0 : 1);
                    case TREELEVEL:
                        return (o1.getTreelevel() > o2.getTreelevel()) ? -1 : ((o1.getTreelevel() == o2.getTreelevel()) ? 0 : 1);
                }
                return -1;
            }
        });
        return guilds;
    }

    public Guild createGuild(String guild, User user){
        Player p = Bukkit.getPlayerExact(user.getPlayer());
        if (p == null){
            return null;
        }
        String owner = user.getPlayer();
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(System.currentTimeMillis());

        LinkedList<String> members = new LinkedList<>();
        members.add(owner);
        Guild guildData = new Guild(guild,owner,"",date,0.0,0.0,0.0,0,0,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new StringStore(),new ArrayList<>(), members,new LinkedList<>(),new Guild.GuildHomeLocation("null",LegendaryGuild.getInstance().SERVER,0.0,0.0,0.0),0);
        cache.put(guild,guildData);
        //更新公会数据库
        updateGuild(guildData,false);

        //设置基础数据
        user.setDate(date);
        user.setGuild(guild);
        user.setPosition(legendaryGuild.getPositionsManager().getOwnerPosition().getId());
        user.setPoints(0.0,false);
        //更新数据库数据以及通知其他子服务器更新数据
        user.update(false);

        //防止残留
        GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild);
        data.setCurrent(new HashMap<>());
        data.setHistory(new HashMap<>());
        data.setTotal_points(0);
        data.setPoints(0);
        data.update();

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
        legendaryGuild.getDataBase().saveGuild(guild);
        if (removeCache){
            cache.remove(guild.getGuild());
            return;
        }
        cache.put(guild.getGuild(),guild);
    }

    public List<String> getGuilds(){
        return legendaryGuild.getDataBase().getGuilds();
    }

    public void loadGuilds(){
        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                getGuilds().forEach(g -> {
                    Guild guild = legendaryGuild.getDataBase().getGuild(g).orElse(null);
                    if (guild != null) {
                        cache.put(g,guild);
                    }
                });
                legendaryGuild.info("载入 "+cache.size()+" 个公会. & Load "+cache.size()+" guild data.", Level.INFO);
            }
        });

    }
}
