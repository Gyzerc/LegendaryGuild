package com.legendaryrealms.LegendaryGuild.API;

import com.legendaryrealms.LegendaryGuild.API.Events.*;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildTeamShopData;
import com.legendaryrealms.LegendaryGuild.Data.Others.TeamShopItem;
import com.legendaryrealms.LegendaryGuild.Data.Others.WaterPot;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Listener.MoveEvent;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import com.legendaryrealms.LegendaryGuild.Utils.RunTaskUtils;
import com.legendaryrealms.LegendaryGuild.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class UserAPI {

    private static final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private static final Lang lang = legendaryGuild.getFileManager().getLang();
    private static final MsgUtils msg = legendaryGuild.getMsgUtils();
    public static User getUser(String playerName){
        return legendaryGuild.getUsersManager().getUser(playerName);
    }
    public static boolean hasGuild(String player){
        return getUser(player).hasGuild();
    }
    public static Optional<Guild> getGuild(String player){
        User user = getUser(player);
        if (user.hasGuild()) {
            return Optional.of(LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild()));
        }
        return Optional.empty();
    }
    public static void sendApplication(User user,String guildName){
        //玩家是否在公会内
        if (!user.getGuild().equals(lang.default_guild)){
            msg.sendMessage(user.getPlayer(),lang.plugin+lang.already_in_guild);
            return;
        }
        //是否存在该公会
        if (!legendaryGuild.getGuildsManager().isExists(guildName)){
            msg.sendMessage(user.getPlayer(),lang.plugin+lang.notguild);
            return;
        }
        //是否处于冷却
        if (user.getCooldown() > 0){
            msg.sendMessage(user.getPlayer(),lang.plugin+lang.create_cooldown.replace("%value%",user.getCooldown()+""));
            return;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);

        LinkedList<Guild.Application> applications=guild.getApplications();
        List<Guild.Application> list = applications.stream().filter(application -> {
            if (application.getPlayer().equals(user.getPlayer())){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        //是否已经发送过申请
        if (list.size() > 0){
            msg.sendMessage(user.getPlayer(),lang.plugin+lang.application_already);
            return;
        }
        //添加到申请列表中
        guild.addApplication(user.getPlayer());
        //更新数据库
        guild.update();


        //发送消息
        msg.sendMessage(user.getPlayer(),lang.plugin+lang.application_send.replace("%value%",guild.getDisplay()));
        msg.sendMessage(guild.getOwner(),lang.plugin+lang.application_recive.replace("%player%",user.getPlayer()));

        //触发事件
        Bukkit.getPluginManager().callEvent(new PlayerApplicationGuildEvent(user, guild));
    }

    public static void giveMoney(Player p,double amount){
        User user = getUser(p.getName());
        if (!user.hasGuild()){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return;
        }
        Guild guild = getGuild(p.getName()).orElse(null);
        if (guild != null){
            if (legendaryGuild.getHookManager().getVaultHook().isEnable()) {
                if (legendaryGuild.getHookManager().getVaultHook().getEconomy().getBalance(p.getName()) >=amount) {
                    double toPoints = legendaryGuild.getFileManager().getConfig().MONEY_TO_POINTS;
                    double addPoints = 0;
                    if (toPoints > 0) {
                        addPoints = amount * toPoints;
                        user.addPoints(addPoints, true);
                        user.update(false);
                    }
                    legendaryGuild.getHookManager().getVaultHook().getEconomy().withdrawPlayer(p,amount);

                    guild.addMoney(amount);
                    guild.update();

                    p.sendMessage(lang.plugin+lang.money_message.replace("%value%",""+amount));
                    legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.money_message_broad.replace("%target%",p.getName()).replace("%value%",""+amount));
                    Bukkit.getPluginManager().callEvent(new GiveGuildMoneyEvent(p,amount));
                    return;
                }
                p.sendMessage(lang.plugin+lang.vault_noenough.replace("%value%",""+amount));
                return;
            }
        }
    }



    public static void JoinGuild(User user,Guild guild){
        if (user.hasGuild()){
            return;
        }
        if (guild.getMembers().size() >= guild.getMaxMembers()){
            return;
        }

        //设置玩家数据
        user.setGuild(guild.getGuild());
        user.setPosition(legendaryGuild.getPositionsManager().getDefaultPosition().getId());
        user.setPoints(0.0,false);
        user.setTotal_points(0.0);
        user.setDate(legendaryGuild.getDate());
        //更新数据库并同步子服数据
        user.update(false);

        //添加成员
        guild.getMembers().add(user.getPlayer());
        //更新数据库发送同步更新信息
        guild.update();

        //发送通知
        msg.sendMessage(user.getPlayer(),lang.plugin+lang.application_join.replace("%value%",guild.getDisplay()));
        msg.sendGuildMessage(guild.getMembers(),lang.plugin+lang.application_join_broad.replace("%value%",user.getPlayer()));

        Bukkit.getPluginManager().callEvent(new PlayerJoinGuildEvent(user,guild));
    }

    public static boolean kick(String guildName, CommandSender sender, String name){
        User user = getUser(name);
        if (user != null) {
            //设置用户数据
            user.setGuild(lang.default_guild);
            user.setPoints(0, false);
            user.setTotal_points(0);
            user.setPosition(lang.default_position);
            user.setCooldown(legendaryGuild.getFileManager().getConfig().COOLDOWN * 60);
            user.setDate("");
            //更新数据库并通知其他子服务器
            user.update(false);
        }


        //设置公会数据
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);
        guild.getMembers().remove(name);
        //更新数据库通知其他子服务器
        guild.update();

        //删除玩家所有的活跃度
        GuildActivityData activityData = legendaryGuild.getGuildActivityDataManager().getData(guildName);
        activityData.clearPlayerData(name);
        activityData.update();

        //发送消息
        if (sender != null){
            sender.sendMessage(lang.plugin+lang.members_kick.replace("%value%",name));
        }
        msg.sendMessage(name,lang.plugin+lang.members_bekick.replace("%value%",guild.getDisplay()));
        msg.sendGuildMessage(guild.getMembers(),lang.plugin+lang.members_kick_broad.replace("%value%",name));

        Bukkit.getPluginManager().callEvent(new PlayerBeKickFromGuildEvent(name,guild));
        return true;
    }


    public static boolean setPlayerPositionByPlayer(Player seter, String target, String positionId){
        if (seter.getName().equals(target)){
            seter.sendMessage(lang.plugin+lang.isowner);
            return false;
        }
        User user = legendaryGuild.getUsersManager().getUser(seter.getName());
        //检测是否有公会
        if (!user.hasGuild()){
            seter.sendMessage(lang.plugin+lang.nothasguild);
            return false;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        //检测玩家是否是会长
        if (!guild.getOwner().equalsIgnoreCase(seter.getName())){
            seter.sendMessage(lang.plugin+lang.notowner);
            return false;
        }
        //检测该玩家在不在公会内
        if (!guild.getMembers().contains(target)){
            seter.sendMessage(lang.plugin+lang.notmember);
            return false;
        }
        //检测目标职位是否是会长
        if (positionId.equalsIgnoreCase(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
            seter.sendMessage(lang.plugin+lang.admin_set_position_cant_owner);
            return false;
        }
        Position position = legendaryGuild.getPositionsManager().getPosition(positionId).orElse(null);
        //检测是否有该职位
        if (position == null){
            seter.sendMessage(lang.plugin+lang.admin_set_position_null);
            return false;
        }
        //检测该职位是否人员达到上限
        if (GuildAPI.getGuildPositionAmount(guild,position) >= position.getMax()){
            seter.sendMessage(lang.plugin+lang.admin_set_position_cant_max);
            return false;
        }

        User TargetUser = legendaryGuild.getUsersManager().getUser(target);
        if (!TargetUser.hasGuild()){
            seter.sendMessage(lang.plugin+lang.admin_target_nothasguild);
            return false;
        }
        if (!TargetUser.getGuild().equals(guild.getGuild())){
            seter.sendMessage(lang.plugin+lang.notmember);
            return false;
        }
        String oldPosition = TargetUser.getPosition();
        TargetUser.setPosition(positionId);
        TargetUser.update(false);

        seter.sendMessage(lang.plugin+lang.positions_message.replace("%target%",target).replace("%value%",position.getDisplay()));
        legendaryGuild.getMsgUtils().sendMessage(target,lang.plugin+lang.positions_message_target.replace("%value%",position.getDisplay()));

        Bukkit.getPluginManager().callEvent(new PlayerPositionChangeEvent(TargetUser,oldPosition,positionId));
        return true;
    }

    public static boolean removePlayerPosition(Player seter,String target){
        User user = legendaryGuild.getUsersManager().getUser(seter.getName());
        //检测是否有公会
        if (!user.hasGuild()){
            seter.sendMessage(lang.plugin+lang.nothasguild);
            return false;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        //检测玩家是否是会长
        if (!guild.getOwner().equalsIgnoreCase(seter.getName())){
            seter.sendMessage(lang.plugin+lang.notowner);
            return false;
        }
        //检测该玩家在不在公会内
        if (!guild.getMembers().contains(target)){
            seter.sendMessage(lang.plugin+lang.notmember);
            return false;
        }

        String defaultId = legendaryGuild.getPositionsManager().getDefaultPosition().getId();
        User targetUser = getUser(target);
        String old = targetUser.getPosition();
        if (old.equals(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
            seter.sendMessage(lang.plugin+lang.admin_set_position_cant);
            return false;
        }
        if (!targetUser.getPosition().equals(defaultId)) {
            targetUser.setPosition(defaultId);
            targetUser.update(false);
            Bukkit.getPluginManager().callEvent(new PlayerPositionChangeEvent(targetUser,old,defaultId));
        }

        seter.sendMessage(lang.plugin+lang.positions_message_cancel.replace("%value%",target));
        legendaryGuild.getMsgUtils().sendMessage(target,lang.plugin+lang.positions_message_cancel_target);

        return true;
    }

    public static boolean GuildTreeWish(User user){
        Player p = Bukkit.getPlayerExact(user.getPlayer());
        if (p != null) {
            if (user.hasGuild()) {
                if (user.isWish()) {
                    legendaryGuild.getMsgUtils().sendMessage(user.getPlayer(), lang.plugin + lang.tree_wish_already);
                    return false;
                }
                user.setWish(true);
                //更新数据库并通知其他子服务器
                user.update(false);

                legendaryGuild.getMsgUtils().sendMessage(user.getPlayer(), lang.plugin + lang.tree_wish);
                Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
                legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(), lang.plugin + lang.tree_wish_broad.replace("%target%", user.getPlayer()));

                int level = guild.getTreelevel();
                List<String> rewards = legendaryGuild.getFileManager().getConfig().WISH.get(level) != null ? legendaryGuild.getFileManager().getConfig().WISH.get(level) : new ArrayList<>();
                RunUtils runUtils = new RunUtils(rewards, p);
                runUtils.start();

                Bukkit.getPluginManager().callEvent(new GuilTreeWishEvent(p,guild));
                return true;
            }
        }
        return false;
    }

    public static boolean GuildTreeWater(Player p,String potId){
        Optional<WaterPot> potOp = legendaryGuild.getWaterPotsManager().getWaterPot(potId);
        if (potOp.isPresent()) {
            User user = legendaryGuild.getUsersManager().getUser(p.getName());
            if (user.hasGuild()) {
                WaterDataStore waterDataStore = user.getWaterDataStore();
                WaterPot pot = potOp.get();

                List<String> requirements = pot.getRequirements();
                if (legendaryGuild.getRequirementsManager().check(p,requirements)) {
                    if (pot.getLimit_day() != -1) {
                        if (waterDataStore.getAmount(potId, WaterDataStore.WaterDataType.TODAY) >= pot.getLimit_day()) {
                            p.sendMessage(lang.plugin + lang.tree_water_limit);
                            return false;
                        }
                    }
                    legendaryGuild.getRequirementsManager().deal(p, requirements);
                    waterDataStore.addAmount(potId, WaterDataStore.WaterDataType.TODAY, 1);
                    waterDataStore.addAmount(potId, WaterDataStore.WaterDataType.TOTAL, 1);
                    user.setWaterDataStore(waterDataStore);
                    user.update(false);

                    p.sendMessage(lang.plugin + lang.tree_water.replace("%value%", pot.getDisplay()));
                    Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
                    legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.tree_water_broad.replace("%target%",p.getName()).replace("%value%",pot.getDisplay()));

                    pot.use(p,user,guild);
                    Bukkit.getPluginManager().callEvent(new GuildTreeWaterEvent(p,pot,guild));
                    return true;
                }
            }
        }
        return false;
    }


    public static void quitGuild(Player p){
        if (!hasGuild(p.getName())){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return;
        }
        User user = getUser(p.getName());
        if (user.getPosition().equals(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
            p.sendMessage(lang.plugin+lang.quit_owner);
            return;
        }
        String guildName = user.getGuild();
        String position = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition()).getDisplay();

        user.setGuild(lang.default_guild);
        user.setPosition(lang.default_position);
        user.setPoints(0,false);
        user.setTotal_points(0);
        user.setCooldown(legendaryGuild.getFileManager().getConfig().COOLDOWN);
        user.update(false);

        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);
        LinkedList<String> members = guild.getMembers();
        members.remove(members.indexOf(p.getName()));
        guild.setMembers(members);
        guild.update();


        //删除玩家所有的活跃度
        GuildActivityData activityData = legendaryGuild.getGuildActivityDataManager().getData(guildName);
        activityData.clearPlayerData(user.getPlayer());
        activityData.update();

        Bukkit.getPluginManager().callEvent(new PlayerQuitGuildEvent(p,guild));

        legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.quit_broad.replace("%value%",p.getName()).replace("%position%",position));
        p.sendMessage(lang.plugin+lang.quit_message.replace("%value%",guild.getDisplay()));
    }


    public static void giveGuild(Player owner,String target){
        User user = getUser(owner.getName());
        if (!user.hasGuild()){
            owner.sendMessage(lang.plugin+lang.nothasguild);
            return;
        }
        if (!user.getPosition().equals(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
            owner.sendMessage(lang.plugin+lang.notowner);
            return;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        User tagetUser = getUser(target);
        if (!tagetUser.hasGuild() || !guild.getMembers().contains(target)){
            owner.sendMessage(lang.plugin+lang.notmember);
            return;
        }

        guild.setOwner(target);
        guild.update();
        user.setPosition(legendaryGuild.getPositionsManager().getDefaultPosition().getId());
        user.update(false);
        tagetUser.setPosition(legendaryGuild.getPositionsManager().getOwnerPosition().getId());
        tagetUser.update(false);


        //删除玩家所有的活跃度
        GuildActivityData activityData = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
        activityData.clearPlayerData(user.getPlayer());
        activityData.update();

        owner.sendMessage(lang.plugin+lang.give_message.replace("%value%",guild.getDisplay()).replace("%target%",target));
        legendaryGuild.getMsgUtils().sendMessage(target,lang.plugin+lang.give_message_target.replace("%value%",guild.getDisplay()));
        legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.give_broad.replace("%value%",target));

        Bukkit.getPluginManager().callEvent(new GuildGiveEvent(owner,target,guild));
    }



    public static void updatePlayerBuffAttribute(Player p){
        if (legendaryGuild.getBuffsManager() != null && legendaryGuild.getBuffsManager().getProvider() != null){
            legendaryGuild.getBuffsManager().getProvider().updateBuff(p);
        }
    }

    private static final Config config = legendaryGuild.getFileManager().getConfig();
    public static void teleportGuildHome(Player p){
        User user = getUser(p.getName());
        if (!user.hasGuild()){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return;
        }
        Guild guild = getGuild(p.getName()).orElse(null);
        if (guild != null){
            Guild.GuildHomeLocation location = guild.getHome();
            if (location == null){
                p.sendMessage(lang.plugin+lang.home_home_null);
                return;
            }

            GuildHomeTeleportEvent event = new GuildHomeTeleportEvent(p);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            MoveEvent.addPlayerWaitTeleport(p.getName());
            new RunTaskUtils(20,config.HOME_WAIT,p)
                    .setTaskEveryPeriod(m -> {
                        if (MoveEvent.hasPlayerWaitTeleport(p.getName())) {
                            if (config.HOME_SOUND_SECOND != null) {
                                p.playSound(p.getLocation(), config.HOME_SOUND_SECOND, 1, 1);
                            }
                            int less = config.HOME_WAIT - m.getSec();
                            p.sendMessage(lang.plugin + lang.home_wait.replace("%value%", "" + less));
                        }
                        else {
                            m.cancel();
                        }
                    })
                    .setConsumerEnd(m -> {
                        teleport(p,location);
                    }).start();
        }
    }


    public static void setGuildHome(Player setter){
        Guild guild = getGuild(setter.getName()).orElse(null);
        if (guild != null){
            if (!guild.getOwner().equals(setter.getName())){
                setter.sendMessage(lang.plugin+lang.notowner);
                return;
            }

            Location loc = setter.getLocation();
            String world = loc.getWorld().getName();
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();

            if (config.HOME_BLACK_SERVER.contains(legendaryGuild.SERVER)){
                setter.sendMessage(lang.plugin+lang.home_cant_server);
                return;
            }
            if (config.HOME_BLACK_WORLD.contains(world)){
                setter.sendMessage(lang.plugin+lang.home_cant_world);
                return;
            }
            Guild.GuildHomeLocation homeLocation = new Guild.GuildHomeLocation(world, legendaryGuild.SERVER, x,y,z);
            guild.setHome(homeLocation);
            guild.update();

            setter.sendMessage(lang.plugin+lang.home_set);
            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin+lang.home_set_broad);

            Bukkit.getPluginManager().callEvent(new GuildHomeChangeEvent(setter,guild,homeLocation));
        }
    }

    private static void teleport(Player p, Guild.GuildHomeLocation location){
        MoveEvent.deletePlayerWaitTeleport(p.getName());
        if (location.getServer().equals(legendaryGuild.SERVER)){
            if (location.getLocation().isPresent()){
                Bukkit.getScheduler().runTask(legendaryGuild,()->
                {
                    p.teleport(location.getLocation().get());
                    p.sendMessage(lang.plugin+lang.home_teleport);
                    if (config.HOME_SOUND_TELEPORT != null) {
                        p.playSound(p.getLocation(), config.HOME_SOUND_TELEPORT, 1, 1);
                    }
                });
            }
        }
        else {

            User user = getUser(p.getName());
            user.setTeleport_guild_home(true);
            user.update(false);

            legendaryGuild.getNetWork().teleportServer(p,location.getServer());
        }
    }
    public static void resetGuildTeamShopData(Guild guild,String player,int amount) {

            GuildTeamShopData teamShopData = LegendaryGuild.getInstance().getGuildTeamShopDataManager().getGuildTeamShopData(guild.getGuild());
            teamShopData.resetPlayerBuys(player,amount);
            teamShopData.update(false);

    }

    public static boolean buyGuildTeamShop(Player p,User user,Guild guild, TeamShopItem shopItem, GuildTeamShopData teamShopData) {

        if (shopItem.getLimit() > 0 && (shopItem.getLimit() <= teamShopData.getBuyAmount(p.getName()))) {
            p.sendMessage(lang.plugin + lang.bargain_buy_limit.replace("%limit%",String.valueOf(shopItem.getLimit())));
            return false;
        }

        boolean canBuy = false;
        double price = teamShopData.getCurrentPrice();;
        switch (shopItem.getCurrency()) {
            case VAULT: {
                if (LegendaryGuild.getInstance().getHookManager().getVaultHook().isEnable()) {
                    if (LegendaryGuild.getInstance().getHookManager().getVaultHook().getEconomy().has(p,price)) {
                        canBuy = true;
                        LegendaryGuild.getInstance().getHookManager().getVaultHook().getEconomy().withdrawPlayer(p,price);
                    }
                    p.sendMessage(lang.plugin + lang.reuirement_notenough_vault.replace("%value%" , String.valueOf(price)));
                    break;
                }
                LegendaryGuild.getInstance().info("检测到未安装 Vault .",Level.SEVERE);
                break;
            }
            case PLAYERPOINTS: {
                if (LegendaryGuild.getInstance().getHookManager().getPlayerPointsHook().isEnable()) {
                    if (LegendaryGuild.getInstance().getHookManager().getPlayerPointsHook().getPlayerPoints().getAPI().look(p.getUniqueId()) >= price) {
                        canBuy = true;
                        LegendaryGuild.getInstance().getHookManager().getPlayerPointsHook().getPlayerPoints().getAPI().take(p.getUniqueId(), (int) price);
                        break;
                    }
                    p.sendMessage(lang.plugin + lang.reuirement_notenough_playerpoints.replace("%value%" , String.valueOf(price)));
                    break;
                }
                LegendaryGuild.getInstance().info("检测到未安装 PlayerPoints",Level.SEVERE);
                break;
            }
            case GUILD_POINTS: {
                if (user.getPoints() >= price) {
                    canBuy = true;
                    user.takePoints(price , false);
                    user.update(false);
                    break;
                }
                break;
            }
        }
        if (canBuy) {
            //增加购买次数
            teamShopData.addBuyAmount(p.getName(),1);

            String display = shopItem.getDisplay();
            p.sendMessage(lang.plugin + lang.bargain_buy.replace("%display%",display)
                    .replace("%price%",String.valueOf(price)));
            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin + lang.bargain_buy_broad.replace("%player%",p.getName())
                    .replace("%display%",display)
                    .replace("%price%",String.valueOf(price)));
            new RunUtils(shopItem.getRun(),p).start();

            Bukkit.getPluginManager().callEvent(new TeamShopBuyEvent(p,shopItem.getId()));
        }
        return canBuy;
    }
}
