package com.legendaryrealms.LegendaryGuild.Data.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Guild {
    private String guild;
    private String icon;
    private String owner;
    private String date;
    private double money;
    private double exp;
    private double treeexp;
    private int level;
    private int treelevel;
    private List<String> intro;
    private List<String> notice;
    private List<String> friends;
    private StringStore buffs;
    private List<String> unlock_icons;
    private LinkedList<String> members;
    private LinkedList<Application> applications;
    private GuildHomeLocation home;
    private int extra_members;

    public Guild(String guild, String owner,String icon, String date, double money, double exp, double treeexp, int level, int treelevel, List<String> intro, List<String> notice, List<String> friends, StringStore buffs, List<String> unlock_icons, LinkedList<String> members, LinkedList<Application> applications, GuildHomeLocation home,int extra_members) {
        this.guild = guild;
        this.icon = icon;
        this.owner = owner;
        this.date = date;
        this.money = money;
        this.exp = exp;
        this.treeexp = treeexp;
        this.level = level;
        this.treelevel = treelevel;
        this.intro = intro;
        this.notice = notice;
        this.friends = friends;
        this.buffs = buffs;
        this.unlock_icons = unlock_icons;
        this.members = members;
        this.applications = applications;
        this.home = home;
        this.extra_members = extra_members;
    }

    public int getExtra_members() {
        return extra_members;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setTreeexp(double treeexp) {
        this.treeexp = treeexp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTreelevel(int treelevel) {
        this.treelevel = treelevel;
    }

    public void setIntro(List<String> intro) {
        this.intro = intro;
    }

    public void setNotice(List<String> notice) {
        this.notice = notice;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setBuffs(StringStore buffs) {
        this.buffs = buffs;
    }

    public void setUnlock_icons(List<String> unlock_icons) {
        this.unlock_icons = unlock_icons;
    }

    public void setMembers(LinkedList<String> members) {
        this.members = members;
    }

    public void setHome(GuildHomeLocation home) {
        this.home = home;
    }

    public String getGuild() {
        return guild;
    }

    public String getOwner() {
        return owner;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public double getMoney() {
        return money;
    }

    public double getExp() {
        return exp;
    }

    public String getIcon() {
        return icon != null ? icon : "";
    }

    public double getTreeexp() {
        return treeexp;
    }

    public int getLevel() {
        return level;
    }

    public int getTreelevel() {
        return treelevel;
    }

    public List<String> getIntro() {
        List<String> i = (intro != null ? intro : new ArrayList<>());
        if (i.size() < LegendaryGuild.getInstance().getFileManager().getConfig().DESC_MAX_LENGTH) {
            return i;
        }
        return new ArrayList<>(i.subList(0,LegendaryGuild.getInstance().getFileManager().getConfig().DESC_MAX_LENGTH));
    }

    public List<String> getNotice() {
        List<String> l = (notice != null ? notice : new ArrayList<>());
        if (l.size() < LegendaryGuild.getInstance().getFileManager().getConfig().NOTICE_MAX_LENGTH) {
            return l;
        }
        return new ArrayList<>(l.subList(0,LegendaryGuild.getInstance().getFileManager().getConfig().NOTICE_MAX_LENGTH));
    }

    public List<String> getFriends() {
        return friends != null ? friends : new ArrayList<>();
    }

    public StringStore getBuffs() {
        return buffs != null ? buffs : new StringStore();
    }

    public List<String> getUnlock_icons() {
        return unlock_icons != null ? unlock_icons : new ArrayList<>();
    }

    public GuildTeamShopData getGuildTeamShopData() {
        return LegendaryGuild.getInstance().getGuildTeamShopDataManager().getGuildTeamShopData(guild);
    }
    public LinkedList<String> getMembers() {
        return members != null ? members : (LinkedList<String>) Arrays.asList(owner);
    }
    public int getMaxMembers() {
        int defaultAmount = LegendaryGuild.getInstance().getFileManager().getConfig().MEMBERS.get(level);
        return defaultAmount + extra_members;
    }

    public void addMoney(double amount){
        money += amount;
    }
    public void takeMoney(double amount){
        money = money-amount >= 0 ? money-amount : 0;
    }
    public LinkedList<Application> getApplications() {
        return applications != null ? applications : new LinkedList<>();
    }

    public GuildHomeLocation getHome() {
        return home;
    }

    public void setExtra_members(int extra_members) {
        this.extra_members = extra_members;
    }

    public void addApplication(String target){
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String date = df.format(System.currentTimeMillis());
        applications.add(new Application(target,date));
    }
    public void removeApplication(String target){
        List<Application> get = applications.stream().filter(a -> {
            if (a.getPlayer().equals(target)){
                return false;
            }
            return true;
        }).collect(Collectors.toList());;
       this.applications = new LinkedList<>(get);
    }

    public void update(){
        LegendaryGuild.getInstance().getGuildsManager().updateGuild(this,false);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_GUILD, guild))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
        }
    }
    public void delete(){
        LegendaryGuild.getInstance().getDataBase().deleteGuild(guild);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.REMOVE_GUILD, guild))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
        }
    }

    public String getDisplay(){
        return LegendaryGuild.getInstance().color(guild);
    }

    public static class GuildHomeLocation {
        private String world;
        private String server;
        private double x;
        private double y;
        private double z;

        public GuildHomeLocation(String world, String server, double x, double y, double z) {
            this.world = world;
            this.server = server;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Optional<Location> toLocation(){
            World world = Bukkit.getWorld(this.world);
            if (world != null){
                Location location = new org.bukkit.Location(world,x,y,z);
                return Optional.of(location);
            }
            return Optional.empty();
        }

        public String getWorld() {
            return world;
        }

        public String getServer() {
            return server;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public String toString(){
            StringBuilder builder=new StringBuilder(server);
            return builder.append(",").append(world).append(",").append(x).append(",").append(y).append(",").append(z).toString();
        }

        public Optional<Location> getLocation(){
            if (Bukkit.getWorld(world) != null){
                return Optional.of(new Location(Bukkit.getWorld(world), x, y, z));
            }
            LegendaryGuild.getInstance().info("该服务器不存在世界："+world, Level.SEVERE);
            return Optional.empty();
        }
    }

    public static class Application {
        private String player;
        private String date;

        public Application(String player, String date) {
            this.player = player;
            this.date = date;
        }

        public String getPlayer() {
            return player;
        }

        public String getDate() {
            return date;
        }
    }


}
