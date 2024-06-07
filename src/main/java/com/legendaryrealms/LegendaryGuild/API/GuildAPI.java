package com.legendaryrealms.LegendaryGuild.API;

import com.legendaryrealms.LegendaryGuild.API.Events.*;
import com.legendaryrealms.LegendaryGuild.Command.AdminCommands.ActivityCommand;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.*;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GuildAPI {

    private static final LegendaryGuild legendaryguild = LegendaryGuild.getInstance();
    private static final Lang lang = legendaryguild.getFileManager().getLang();
    private static final Config config = legendaryguild.getFileManager().getConfig();

    public static Optional<Guild> getGuild(String name) {
        Guild guild = legendaryguild.getGuildsManager().getGuild(name);
        return guild != null ? Optional.of(guild) : Optional.empty();
    }
    public static boolean createGuild(User user,String guild){
        if (Bukkit.getPlayer(user.getPlayer()) == null){
            return false;
        }
        Player p = Bukkit.getPlayer(user.getPlayer());
        //是否处于冷却
        if (user.getCooldown() > 0){
            p.sendMessage(lang.plugin+lang.create_cooldown.replace("%value%",user.getCooldown()+""));
            return false;
        }
        //是否在公会内
        if (user.hasGuild()){
            p.sendMessage(lang.plugin+lang.already_in_guild);
            return false;
        }
        //获取玩家权限对应的创建组
        CreateGuildSection section = legendaryguild.getCreateGroupsManager().getPlayerGroup(p).orElse(null);
        if (section == null){
            legendaryguild.info("创建公会的默认组缺失,请重新生成config.yml...", Level.SEVERE);
            return false;
        }
        //检测是否符合创建条件组
        if (!legendaryguild.getCreateGroupsManager().checkGroup(p,section)){
            return false;
        }
        //检测名字是否过长
        if (guild.length() > config.max_length){
            p.sendMessage(lang.plugin+lang.create_length);
            return false;
        }
        //检测是否有同名的公会
        if (legendaryguild.getGuildsManager().getGuilds().contains(guild)){
            p.sendMessage(lang.plugin+lang.create_exists);
            return false;
        }
        //处理条件组
        legendaryguild.getCreateGroupsManager().dealGroup(p,section);

        //创建公会..
        Guild data = legendaryguild.getGuildsManager().createGuild(guild,user);

        //发送消息
        legendaryguild.getMsgUtils().sendMessage(p.getName(),lang.plugin+lang.create_message.replace("%value%",data.getDisplay()));
        lang.create_broad.forEach(msg -> {
            legendaryguild.getMsgUtils().sendBroad(msg.replace("%value%",data.getDisplay()).replace("%target%",user.getPlayer()));
        });


        //触发事件
        Bukkit.getPluginManager().callEvent(new CreateGuildEvent(p,data));
        return true;
    }

    public static int getGuildPositionAmount(Guild guild, Position position){
        List<String> samePositionMembers = guild.getMembers().stream().filter(member -> {
            User target = LegendaryGuild.getInstance().getUsersManager().getUser(member);
            return target.getPosition().equals(position.getId());
        }).collect(Collectors.toList());
        return samePositionMembers.size();
    }

    public static void unlockStore(Player unlocker,Guild guild,int id){
        GuildStore store = legendaryguild.getStoresManager().getStore(guild.getGuild());
        store.unlock(id);
        //发送新消息
        if (unlocker != null) {
            unlocker.sendMessage(lang.plugin + lang.stores_unlock.replace("%value%", id + ""));
        }
        legendaryguild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.stores_unlock_broad.replace("%value%",""+id));
        //更新数据库通知其他子服务器更新数据
        store.update();
    }

    public static void createRedPacket(Player p,double total,int amount){
        if (legendaryguild.getHookManager().getVaultHook().isEnable()) {
            User user = legendaryguild.getUsersManager().getUser(p.getName());
            if (!user.hasGuild()) {
                p.sendMessage(lang.plugin + lang.nothasguild);
                return;
            }
            Guild guildData = legendaryguild.getGuildsManager().getGuild(user.getGuild());
            if (total < config.MIN_REDPACKET_TOTAL) {
                p.sendMessage(lang.plugin + lang.redpacket_min_total);
                return;
            }
            if (amount < config.MIN_REDPACKET_AMOUNT) {
                p.sendMessage(lang.plugin + lang.redpacket_min_amount);
                return;
            }
            if (amount > guildData.getMembers().size()){
                p.sendMessage(lang.plugin+lang.redpacket_create_amount_max);
                return;
            }
            if (legendaryguild.getHookManager().getVaultHook().getEconomy().getBalance(p) < total){
                p.sendMessage(lang.plugin+lang.vault_noenough.replace("%value%",total+""));
                return;
            }

            legendaryguild.getHookManager().getVaultHook().getEconomy().withdrawPlayer(p,total);

            UUID uuid = UUID.randomUUID();
            Guild_Redpacket redpackets = legendaryguild.getDataBase().getRedPacket(guildData.getGuild());
            Guild_Redpacket.Redpacket redpacket = new Guild_Redpacket.Redpacket(uuid, legendaryguild.getDate(), p.getName(), total, total, amount, new HashMap<>());
            HashMap<UUID, Guild_Redpacket.Redpacket> list = redpackets.getRedpackets();
            list.put(uuid,redpacket);
            redpackets.setRedpackets(list);
            //更新数据库并通知其他子服务器
            legendaryguild.getRedPacketsManager().updateRedPacket(redpackets);

            //发送消息
            p.sendMessage(lang.plugin+lang.redpacket_create.replace("%total%",""+total).replace("%amount%",""+amount));
            legendaryguild.getMsgUtils().sendGuildMessage(guildData.getMembers(),lang.plugin+lang.redpacket_create_broad.replace("%target%",p.getName()).replace("%total%",""+total).replace("%amount%",""+amount));
        }
    }


    public static void addGuildLevel(Guild guild,int amount){
        int level = guild.getLevel();
        if (level == config.MAXLEVEL){
            return;
        }
        int add = level+amount <= config.MAXLEVEL ? amount : config.MAXLEVEL-level;
        guild.setLevel(level+add);
        guild.update();

        legendaryguild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.level_levelup.replace("%value%",""+(level+add)));
        lang.level_levelup_broad.forEach(msg -> {
            legendaryguild.getMsgUtils().sendBroad(msg.replace("%target%",guild.getDisplay()).replace("%value%",""+(level+add)));
        });

        Bukkit.getPluginManager().callEvent(new GuildLevelupEvent(guild,level,(level+add)));
    }

    public static void takeGuildLevel(Guild guild,int amount){

        int level = guild.getLevel();
        if (level == 0){
            return;
        }
        int take = level -amount >= 0 ? amount : level;
        guild.setLevel(level-take);
        guild.update();

    }

    public static void setGuildLevel(Guild guild,int amount){
        if (amount >= 0) {
            int set = Math.min(amount, config.MAXLEVEL);
            guild.setLevel(set);
            guild.update();
        }
    }


    public static void addGuildExp(String player,Guild guild,double amount){
        int level = guild.getLevel();
        if (level == config.MAXLEVEL){
            return;
        }
        double exp = guild.getExp();
        double total = exp + amount;
        double next = config.EXP.get(level);
        if (amount > 0) {
            legendaryguild.getMsgUtils().sendMessage(player, lang.plugin + lang.level_expadd.replace("%value%", amount + ""));
        }
        //检测是否可以升级
        if (total >= next) {
            double less = total - next;
            level+=1;

            guild.setExp(less);
            guild.update();

            //升级
            addGuildLevel(guild,1);
            //检测是否还能升级
            if (level < config.MAXLEVEL && less >= config.EXP.get(level)){
                //再次进行判定
                addGuildExp(player,guild,0);
            }
        }
        else {
            guild.setExp(total);
            guild.update();
        }

        Bukkit.getPluginManager().callEvent(new GuildExpChangeEvent(guild,amount));
    }

    public static void takeGuildExp(Guild guild,double amount){
        int level = guild.getLevel();
        double exp = guild.getExp();
        if (level <= 0){
            double take = exp -amount >= 0 ? amount : exp;
            guild.setExp(exp - take);
            guild.update();
            return;
        }
        if (exp - amount >= 0){
            guild.setExp(exp - amount);
            guild.update();
            return;
        }
        guild.setExp(config.EXP.get(level-1));
        guild.update();
        double less = amount - exp;
        takeGuildLevel(guild,1);
        takeGuildExp(guild,less);

        Bukkit.getPluginManager().callEvent(new GuildExpChangeEvent(guild,amount));
    }

    public static void setGuildExp(Guild guild,double amount){
        if (amount >= 0){
            if (guild.getLevel() >= config.MAXLEVEL){
                return;
            }
            double set = Math.min(amount,config.EXP.get(guild.getLevel()));
            guild.setExp(set);
            guild.update();

            Bukkit.getPluginManager().callEvent(new GuildExpChangeEvent(guild,amount));
        }
    }

    public static void addGuildTreeLevel(String player,Guild guild,int amount){
        int level = guild.getLevel();
        int treelevel = guild.getTreelevel();
        if (treelevel+1 > level){
            legendaryguild.getMsgUtils().sendMessage(player,lang.plugin+lang.tree_level_large);
            return;
        }
        if (treelevel == config.MAX_TREE_LEVEL){
            legendaryguild.getMsgUtils().sendMessage(player,lang.plugin+lang.tree_level_max);
            return;
        }
        int up = amount + treelevel <= config.MAX_TREE_LEVEL ? amount : config.MAX_TREE_LEVEL-treelevel;
        int finalLevel = treelevel+up;
        legendaryguild.getMsgUtils().sendMessage(player,lang.plugin+lang.tree_levelup_byplayer);
        legendaryguild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.tree_levelup.replace("%value%",""+finalLevel));


        guild.setTreelevel(finalLevel);
        //更新数据库并通知其他子服务器
        guild.update();

        Bukkit.getPluginManager().callEvent(new GuildTreeLevelupEvent(guild,treelevel,finalLevel));
    }

    public static boolean addGuildTreeLevelByPlayer(Player p){
        User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
        if (!user.hasGuild()){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return false;
        }
        Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
        if (!guild.getOwner().equals(p.getName())){
            p.sendMessage(lang.plugin+lang.notowner);
            return false;
        }
        int level = guild.getTreelevel();
        double exp = guild.getTreeexp();
        double next = config.TREEEXP.get(level);
        if (level + 1 > guild.getLevel()){
            p.sendMessage(lang.plugin+lang.tree_level_large);
            return false;
        }
        if (exp < next){
            p.sendMessage(lang.plugin+lang.tree_levelup_cant);
            return false;
        }
        List<String> requirements = config.TREE_REQUIREMENTS.get(level);
        if (legendaryguild.getRequirementsManager().check(p,requirements)){
            legendaryguild.getRequirementsManager().deal(p,requirements);

            guild.setTreeexp(0);
            guild.update();

            addGuildTreeLevel(p.getName(),guild,1);
            return true;
        }
        return false;

    }


    public static void takeGuildTreeLevel(String player,Guild guild,int amount) {
        int level = guild.getLevel();
        int take = level-amount >= 0 ? amount : level;
        guild.setTreelevel(level-take);
        //更新数据库并通知其他子服务器
        guild.update();
    }

    public static void setGuildTreeLevel(String player,Guild guild,int amount) {
        if (amount >= 0) {
            int set = Math.min(amount, config.MAX_TREE_LEVEL);
            guild.setTreelevel(set);
            //更新数据库并通知其他子服务器
            guild.update();
        }
    }

    public static void addGuildTreeExp(String player,Guild guild,double amount){
        if (guild.getTreelevel() == config.MAX_TREE_LEVEL){
            return;
        }
        double exp = guild.getTreeexp();
        double next =  config.TREEEXP.get(guild.getTreelevel());
        double set = Math.min(exp + amount, next);
        guild.setTreeexp(set);
        guild.update();

        legendaryguild.getMsgUtils().sendMessage(player,lang.plugin+lang.tree_expadd_byplayer.replace("%target%",guild.getDisplay()).replace("%value%",(set-exp)+""));
        Bukkit.getPluginManager().callEvent(new GuildTreeExpChangeEvent(guild,set));
    }

    public static void takeGuildTreeExp(String player,Guild guild,double amount){
        double exp = guild.getTreeexp();
        double take = exp - amount >= 0 ? amount : exp;
        guild.setTreeexp(exp - take);
        guild.update();

        Bukkit.getPluginManager().callEvent(new GuildTreeExpChangeEvent(guild,(exp - take)));
    }

    public static void setGuildTreeExp(String player,Guild guild,double amount){
        if (amount >= 0) {
            if (guild.getTreelevel() == config.MAX_TREE_LEVEL){
                return;
            }
            double next = config.TREEEXP.get(guild.getTreelevel());
            double set = Math.min(amount,next);
            guild.setTreeexp(set);
            guild.update();

            Bukkit.getPluginManager().callEvent(new GuildTreeExpChangeEvent(guild,set));
        }
    }

    public static String getGuildTreeExpProgressBar(Guild guild){
        StringBuilder builder = new StringBuilder();
        if (config.TREE_BAR_LENGTH > 0) {
            int length = config.TREE_BAR_LENGTH;

            int level = guild.getTreelevel();
            double exp = guild.getTreeexp();
            double next = level + 1 <= config.MAX_TREE_LEVEL ? config.TREEEXP.get(level) : exp;


            float percent = Float.isNaN((float) (exp / next)) ? 100.0F : (float) Math.abs(Math.min(exp / next, 100.0F));
            int completedAmount = (int) (percent * length);

            int a = 0;
            while (a < length){
                if (a < completedAmount){
                 builder.append(config.TREE_BAR_COMPLETED);
                } else {
                    builder.append(config.TREE_BAR_EMPTRY);
                }
                a++;
            }
        }
        return builder.toString();
    }


    public static boolean addGuildBuffLevel(Guild guild,Player owner, Buff buff){

        StringStore data = guild.getBuffs();
        int current = Integer.parseInt(data.getValue(buff.getId(),0).toString());
        if (current >= guild.getLevel()){
            owner.sendMessage(lang.plugin+lang.buff_cant);
            return false;
        }
        int max = buff.getMax();
        if (current >= max) {
            owner.sendMessage(lang.plugin+lang.buff_max);
            return false;
        }

        List<String> requirements = buff.getRequirements(current + 1);
        if (!legendaryguild.getRequirementsManager().check(owner,requirements)){
            return false;
        }
        legendaryguild.getRequirementsManager().deal(owner,requirements);

        //处理等级并同步数据
        data.setValue(buff.getId(), (current+1) ,1);
        guild.setBuffs(data);
        guild.update();

        //更新buff属性
        updateGuildMembersBuff(guild);

        legendaryguild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.buff_levelup.replace("%target%",buff.getDisplay()).replace("%value%",""+(current+1)));
        Bukkit.getPluginManager().callEvent(new GuildBuffLevelupEvent(guild,buff,1));
        return true;
    }

    public static void deleteGuild(Guild guild){
        guild.getMembers().forEach(m -> {

            User user = UserAPI.getUser(m);
            user.setGuild(lang.default_guild);
            user.setPosition(lang.default_position);
            user.setPoints(0,false);
            user.setTotal_points(0);
            user.update(false);

            //发送消息
            legendaryguild.getMsgUtils().sendMessage(m,lang.plugin+lang.delete_broad_members);
        });

        //发送通报
        lang.delete_broad.forEach(msg -> {
            legendaryguild.getMsgUtils().sendBroad(msg.replace("%value%",guild.getDisplay()));
        });

        //移除公会数据并同步至其他子服务器
        guild.delete();
        Bukkit.getPluginManager().callEvent(new GuildDeleteEvent(guild));
    }

    public static void addGuildActivity(Player p, Guild guild, double amount, ActivityCommand.AddType type){

        Guild addGuild = guild;
        GuildActivityData data = null;
        if (type.equals(ActivityCommand.AddType.PLAYER)) {
            if (p != null) {
                User user = UserAPI.getUser(p.getName());
                if (!user.hasGuild()) {
                    return;
                }
                addGuild = legendaryguild.getGuildsManager().getGuild(user.getGuild());
                data = legendaryguild.getGuildActivityDataManager().getData(addGuild.getGuild());

                p.sendMessage(lang.plugin + lang.activity_gain.replace("%value%", "" + amount));

                data.setPlayerHistoryActivity(p.getName(), data.getPlayerTotalActivity(p.getName()) + amount);
                data.setPlayerActivity(p.getName(), data.getPlayerActivity(p.getName()) + amount);
            }
        } else {
            data = legendaryguild.getGuildActivityDataManager().getData(addGuild.getGuild());
        }
        data.setPoints( data.getPoints() + amount);
        data.setTotal_points( data.getTotal_points() + amount);
        data.update();

        Bukkit.getPluginManager().callEvent(new GuildActivityChangeEvent(addGuild, GuildActivityChangeEvent.ChangeType.Add,amount));
    }

    public static double takeGuildActivity(Player p, Guild guild,double amount, ActivityCommand.AddType type){
        Guild takeGuild = guild;
        if (type.equals(ActivityCommand.AddType.PLAYER)){
            if (p==null){
                return 0.0;
            }
            User user = UserAPI.getUser(p.getName());
            if (!user.hasGuild()){
                return 0.0;
            }
            takeGuild = legendaryguild.getGuildsManager().getGuild(user.getGuild());
        }

        GuildActivityData data = legendaryguild.getGuildActivityDataManager().getData(takeGuild.getGuild());
        double take = Math.min(data.getPoints(), amount);
        data.setPoints( data.getPoints() - take);
        data.update();

        Bukkit.getPluginManager().callEvent(new GuildActivityChangeEvent(takeGuild, GuildActivityChangeEvent.ChangeType.Take,take));

        return take;
    }

    public static double setGuildActivity(Player p, Guild guild,double amount, ActivityCommand.AddType type){
        Guild setGuild = guild;
        if (type.equals(ActivityCommand.AddType.PLAYER)){
            if (p==null){
                return 0.0;
            }
            User user = UserAPI.getUser(p.getName());
            if (!user.hasGuild()){
                return 0.0;
            }
            setGuild = legendaryguild.getGuildsManager().getGuild(user.getGuild());
        }

        GuildActivityData data = legendaryguild.getGuildActivityDataManager().getData(setGuild.getGuild());
        double set = Math.min(0,amount);
        data.setPoints(set);
        data.update();

        Bukkit.getPluginManager().callEvent(new GuildActivityChangeEvent(setGuild, GuildActivityChangeEvent.ChangeType.Set,set));

        return set;
    }

    public static void updateGuildMembersBuff(Guild guild) {
        if (legendaryguild.getBuffsManager().getProvider() != null) {
            guild.getMembers().stream().filter(member -> {
                if (Bukkit.getPlayerExact(member) != null) {
                    return true;
                }
                return false;
            }).map(m -> Bukkit.getPlayerExact(m)).collect(Collectors.toList()).forEach(p -> {
                legendaryguild.getBuffsManager().getProvider().updateBuff(p);
            });
        }
    }

    public static void resetGuildTeamShopData(Guild guild) {
        GuildTeamShopData teamShopData = guild.getGuildTeamShopData();
        teamShopData.clearBuys();
        teamShopData.update(false);
    }

    public static void refreshGuildTeamShopItem(Guild guild) {
        GuildTeamShopData teamShopData = guild.getGuildTeamShopData();
        teamShopData.randomShop();
        teamShopData.update(false);
    }
}
