package com.legendaryrealms.LegendaryGuild.Data.User;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.logging.Level;

public class User {
    private final Lang lang = LegendaryGuild.getInstance().getFileManager().getLang();
    private String player;
    private String guild;
    private String position;
    private String date;
    private WaterDataStore waterDataStore;
    private int cooldown;
    private boolean wish;
    private boolean teleport_guild_home;
    private double points;
    private double total_points;
    private PvpType pvp;
    private boolean chat;

    public User(String player, String guild, String position, String date, WaterDataStore waterDataStore, int cooldown, boolean wish, boolean teleport_guild_home, double points, double total_points,PvpType pvp ) {
        this.player = player;
        this.guild = guild;
        this.position = position;
        this.date = date;
        this.waterDataStore = waterDataStore;
        this.cooldown = cooldown;
        this.wish = wish;
        this.teleport_guild_home = teleport_guild_home;
        this.points = points;
        this.total_points = total_points;
        this.pvp = pvp;
        this.chat = false;
    }


    public boolean isChat() {
        return chat;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }


    public boolean hasGuild(){
        if (guild == null || guild.isEmpty() || (guild != null && guild.equals(lang.default_guild))){
            return false;
        }
        Guild guild = GuildAPI.getGuild(this.guild).orElse(null);
        if (guild == null) {
            LegendaryGuild.getInstance().info("检测到玩家"+player+"目前所在的公会:"+this.guild+"并不存在, 已重置该玩家的公会数据...", Level.SEVERE);
            LegendaryGuild.getInstance().info("Detected player "+player+"'s current guild: "+this.guild+" does not exist. The guild data for this player has been reset", Level.SEVERE);
            setGuild(lang.default_guild);
            setTeleport_guild_home(false);
            setChat(false);
            setPosition(lang.default_position);
            setPoints(0,false);
            setTotal_points(0);
            update(false);
            return false;
        }
        return true;
    }
    public PvpType getPvp() {
        return pvp;
    }

    public String getPlayer() {
        return player;
    }

    public String getGuild() {
        return guild;
    }

    public String getPosition() {
        return position;
    }

    public String getDate() {
        return date;
    }

    public WaterDataStore getWaterDataStore() {
        return waterDataStore;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isWish() {
        return wish;
    }

    public boolean isTeleport_guild_home() {
        return teleport_guild_home;
    }

    public double getPoints() {
        return points;
    }

    public double getTotal_points() {
        return total_points;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setPvp(PvpType pvp) {
        this.pvp = pvp;
    }

    public void setWish(boolean wish) {
        this.wish = wish;
    }

    public void setWaterDataStore(WaterDataStore waterDataStore) {
        this.waterDataStore = waterDataStore;
    }

    public void setTeleport_guild_home(boolean teleport_guild_home) {
        this.teleport_guild_home = teleport_guild_home;
    }



    public void setTotal_points(double total_points) {
        this.total_points = total_points;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addPoints(double amount,boolean sendMsg){
        this.points += amount;
        this.total_points += amount;
        if (sendMsg){
            LegendaryGuild.getInstance().getMsgUtils().sendMessage(player,lang.plugin+lang.uset_recive_points.replace("%value%",amount+""));
        }
    }
    public void takePoints(double amount,boolean sendMsg){
        this.points = this.points - amount >= 0? this.points - amount : 0;
        if (sendMsg){
            LegendaryGuild.getInstance().getMsgUtils().sendMessage(player,lang.plugin+lang.uset_decrease_points.replace("%value%",""+amount));
        }
    }
    public void setPoints(double points,boolean sendMsg) {
        this.points = points;

        if (sendMsg){
            LegendaryGuild.getInstance().getMsgUtils().sendMessage(player,lang.plugin+lang.uset_set_points.replace("%value%",points+""));
        }
    }


    public void update(boolean r) {
        LegendaryGuild.getInstance().getUsersManager().updateUser(this, r);
        if (LegendaryGuild.getInstance().getNetWork().isEnable()) {
            Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
            if (p != null) {
                new NetWorkMessageBuilder()
                        .setReciver("ALL")
                        .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_USER, this.player))
                        .setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                        .sendPluginMessage(p);
            }
        }
    }

    public enum PvpType {
        ALL,
        NO_SAME_GUILD,
        BLACK_GUILD;
        public static PvpType getType(String str){
            try {
                PvpType type = PvpType.valueOf(str);
                return type;
            } catch (IllegalArgumentException e){
                return ALL;
            }
        }
    }
}
