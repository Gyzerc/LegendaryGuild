package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild_Redpacket;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildRedPacketsManager {
    private HashMap<String, Guild_Redpacket> cache;
    private LegendaryGuild legendaryGuild;
    private Lang lang;
    public GuildRedPacketsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.lang = legendaryGuild.getFileManager().getLang();
        this.cache = new HashMap<>();
    }

    public Guild_Redpacket getRedPacketData(String guild){
        if (cache.containsKey(guild)){
            return cache.get(guild);
        }
        Guild_Redpacket redpacket = legendaryGuild.getDataBase().getRedPacket(guild);
        cache.put(guild,redpacket);
        return redpacket;
    }

    public boolean grabRedPacket(String guildName, UUID uuid,Player p){
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);

        Guild_Redpacket guild_redpacket = getRedPacketData(guildName);
        Guild_Redpacket.Redpacket redpacket=guild_redpacket.getRedpackets().get(uuid);

        //检测是否已经领取过
        HashMap<String,Double> history = redpacket.getHistory();
        if (history.containsKey(p.getName())){
            p.sendMessage(lang.plugin+lang.redpacket_garb_already);
            return false;
        }

        int lessAmount = redpacket.getAmount() - redpacket.getHistory().size();
        if (redpacket.getLess() <= 0 || lessAmount <=0){
            p.sendMessage(lang.plugin+lang.redpacket_garb_no);
            return false;
        }

        HashMap<UUID, Guild_Redpacket.Redpacket> map = guild_redpacket.getRedpackets();

        double less = redpacket.getLess();
        double finaAmount = less;

        //如果只剩下最后一份则获取红包剩余的金额并删去红包缓存
        if (lessAmount == 1){
            //加入领取记录
            history.put(p.getName(),finaAmount);
            //排序领取记录并获取第一名
            //发送消息
            RedPacketHistoryData first = getFirst(history);
            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin + lang.redpacket_garb.replace("%target%",redpacket.getPlayer()).replace("%value%",p.getName()).replace("%money%",""+finaAmount).replace("%less%",""+lessAmount));
            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.redpacket_garb_finally.replace("%target%",redpacket.getPlayer()).replace("%luck%",""+first.getPlayer()).replace("%value%",first.getAmount()+""));

            //更新数据
            map.remove(uuid);
            guild_redpacket.setRedpackets(map);

        }
        else {
            //随机金额
            double maxRoll =( redpacket.getTotal() / redpacket.getAmount() ) * 2;
            finaAmount = (new Random()).nextInt((int) (maxRoll * 10)) / 10;

            //更新数据
            history.put(p.getName(),finaAmount);
            redpacket.setLess(less - finaAmount >= 0 ? less-finaAmount : 0);
            map.put(uuid,redpacket);

            //排序领取记录并获取第一名
            //发送消息
            RedPacketHistoryData first = getFirst(history);
            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin + lang.redpacket_garb.replace("%target%",redpacket.getPlayer()).replace("%value%",p.getName()).replace("%money%",""+finaAmount).replace("%less%",""+lessAmount));

        }

        //更新数据库并通知其他子服务器
        updateRedPacket(guild_redpacket);
        //给与奖励
        legendaryGuild.getHookManager().getVaultHook().getEconomy().depositPlayer(p,finaAmount);

        return true;
    }

    public void reloadRedPacket(String guild){
        cache.remove(guild);
    }
    public void updateRedPacket(Guild_Redpacket redpacket){
        legendaryGuild.getDataBase().saveRedPacket(redpacket);
        cache.remove(redpacket.getGuild());

        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            //通知其他子服务器
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setReciver("ALL")
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_REDPACKEY, redpacket.getGuild()))
                    .sendPluginMessage(p);
        }
    }

    public RedPacketHistoryData getFirst(HashMap<String,Double> map){
        if (map == null){
            return null;
        }
        if (map.isEmpty() ){
            return null;
        }
        List<RedPacketHistoryData> list = new ArrayList<>();
        map.forEach((p,a)->{
            list.add(new RedPacketHistoryData(p,a));
        });
        Collections.sort(list, new Comparator<RedPacketHistoryData>() {
            @Override
            public int compare(RedPacketHistoryData o1, RedPacketHistoryData o2) {
                return (o1.getAmount() > o2.getAmount()) ? -1 : ((o1.getAmount() == o2.getAmount()) ? 0 : 1);
            }
        });
        return list.get(0);
    }


    public class RedPacketHistoryData {
        private String player;
        private double amount;

        public RedPacketHistoryData(String player, double amount) {
            this.player = player;
            this.amount = amount;
        }

        public String getPlayer() {
            return player;
        }

        public double getAmount() {
            return amount;
        }
    }
}
