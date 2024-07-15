package com.legendaryrealms.LegendaryGuild.API;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildTeamShopData;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.Others.TeamShopItem;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildListPanel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class LegendaryGuildPlaceholderAPI extends PlaceholderExpansion {
    public LegendaryGuildPlaceholderAPI() {
        if (legendaryGuild.getHookManager().getPlaceholderAPIHook().isEnable()){
            this.register();
        }
    }

    @Override
    public String getIdentifier() {
        return "LegendaryGuild";
    }

    @Override
    public String getAuthor() {
        return "Gyzer";
    }

    @Override
    public String getVersion() {
        return "5.1.8.4";
    }

    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private final Lang lang = legendaryGuild.getFileManager().getLang();
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        User user = UserAPI.getUser(player.getName());
        Guild guild = UserAPI.getGuild(player.getName()).orElse(null);
        if (params.startsWith("other_")) {
            String deal = params.replace("other_","");
            String name = getName(deal);
            String met = deal.replace("{"+name+"}_","");
            if (name != null) {
                user = UserAPI.getUser(name);
                guild = UserAPI.getGuild(name).orElse(null);
                GuildActivityData data = null;
                switch (met) {
                    case "guild":
                        return guild != null ? guild.getDisplay() : lang.default_guild;
                    case "position":
                        Position position = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition());
                        return position.getDisplay();
                    case "points":
                        return String.valueOf(user.getPoints());
                    case "points_total":
                        return String.valueOf(user.getTotal_points());
                    case "activity":
                        if (guild != null) {
                            data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                            return String.valueOf(data.getPlayerActivity(name));
                        }
                        return "0.0";
                    case "total_activity":
                        if (guild != null) {
                            data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                            return String.valueOf(data.getPlayerTotalActivity(name));
                        }
                        return "0.0";
                    case "guild_members":
                        return guild != null ? String.valueOf(guild.getMembers().size()) : "0";
                    case "guild_maxmembers":
                        int max = guild.getMaxMembers();
                        return String.valueOf(max);
                    case "guild_level":
                        return guild != null ? String.valueOf(guild.getLevel()) : "0";
                    case "guild_exp":
                        return guild != null ? String.valueOf(guild.getExp()) : "0.0";
                    case "guild_level_next":
                        double next = legendaryGuild.getFileManager().getConfig().EXP.get(guild.getLevel());
                        return String.valueOf(next);
                    case "guild_tree_level":
                        return guild != null ? String.valueOf(guild.getTreelevel()) : "0";
                    case "guild_tree_exp":
                        return guild != null ? String.valueOf(guild.getTreeexp()) : "0.0";
                    case "guild_tree_next":
                        next = legendaryGuild.getFileManager().getConfig().TREEEXP.getOrDefault(guild.getTreelevel(), -1.0);
                        return next + "";
                    case "guild_money":
                        return guild != null ? String.valueOf(guild.getMoney()) : "0.0";
                    case "guild_tree_next_bar":
                        return guild != null ? GuildAPI.getGuildTreeExpProgressBar(guild) : "";
                }
            }
            return "";
        }
        if (params.equals("guild")) {
            return guild != null ? guild.getDisplay() : lang.default_guild;
        }
        if (params.equals("position")) {
            Position position = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition());
            return position.getDisplay();
        }
        if (params.equals("points")) {
            return user.getPoints() + "";
        }
        if (params.equals("total_points")) {
            return user.getTotal_points() + "";
        }
        if (params.equals("activity")) {
            if (guild != null) {
                GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                return String.valueOf(data.getPlayerActivity(player.getName()));
            }
            return "0.0";
        }
        if (params.equals("total_activity")) {
            if (guild != null) {
                GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                return String.valueOf(data.getPlayerTotalActivity(player.getName()));
            }
            return "0.0";
        }
        if (params.equals("guild_members")) {
            return (guild != null) ? (guild.getMembers().size() + "") : "0";
        }
        if (params.equals("guild_maxmembers")) {
            int max = guild.getMaxMembers();
            return ""+max;
        }
        if (params.equals("guild_level_next")) {
            double next = legendaryGuild.getFileManager().getConfig().EXP.get(guild.getLevel());
            return String.valueOf(next);
        }
        if (params.equals("guild_level"))
            return (guild != null) ? (guild.getLevel() + "") : "0";
        if (params.equals("guild_exp"))
            return (guild != null) ? (guild.getExp() + "") : "0.0";
        if (params.equals("guild_tree_level"))
            return (guild != null) ? (guild.getTreelevel() + "") : "0";
        if (params.equals("guild_tree_exp"))
            return (guild != null) ? (guild.getTreeexp() + "") : "0.0";
        if (params.equals("guild_tree_next")) {
            double next = legendaryGuild.getFileManager().getConfig().TREEEXP.getOrDefault(guild.getTreelevel(), -1.0);
            return next + "";
        }
        if (params.equals("guild_tree_next_bar")) {
            return GuildAPI.getGuildTreeExpProgressBar(guild);
        }
        if (params.equals("guild_money"))
            return (guild != null) ? (guild.getMoney() + "") : "0.0";
        if (params.equals("guild_activity")) {
            if (guild != null) {
                GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                return String.valueOf(data.getPoints());
            }
            return "0.0";
        }
        if (params.equals("guild_total_activity")) {
            if (guild != null) {
                GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
                return String.valueOf(data.getTotal_points());
            }
            return "0.0";
        }
        if (params.startsWith("teamshop_")) {
            if (guild != null) {
                GuildTeamShopData shopData = guild.getGuildTeamShopData();
                TeamShopItem shopItem = legendaryGuild.getTeamShopManager().getShopItem(shopData.getTodayShopId());
                String id = params.replace("teamshop_","");
                switch (id) {
                    case "current_price" : {
                        return String.valueOf(shopData.getCurrentPrice());
                    }
                    case "base_price" : {
                        TeamShopItem item = legendaryGuild.getTeamShopManager().getShopItem(shopData.getTodayShopId());
                        return String.valueOf(item != null ? item.getPRICE_BASE() : 0.0);
                    }
                    case "amount" : {
                        return String.valueOf(shopData.getBuyAmount(player.getName()));
                    }
                    case "bargain" : {
                        return String.valueOf(shopData.getBargainPrice(player.getName()));
                    }
                    case "bargain_members" : {
                        return String.valueOf(shopData.getBargains().size());
                    }
                    case "id" : {
                        return shopData.getTodayShopId();
                    }
                    case "display" : {
                        return shopItem.getDisplay();
                    }
                }
            }
            return "0";
        }
        if (params.startsWith("player_pot_day_")) {
            String id = params.replace("player_pot_day_", "");
            WaterDataStore waterDataStore = user.getWaterDataStore();
            return ""+waterDataStore.getAmount(id, WaterDataStore.WaterDataType.TODAY);
        }
        if (params.startsWith("player_pot_total_")) {
            String id = params.replace("player_pot_total_", "");
            WaterDataStore waterDataStore = user.getWaterDataStore();
            return ""+waterDataStore.getAmount(id, WaterDataStore.WaterDataType.TOTAL);
        }
        if (params.startsWith("guild_buff_level_")) {
            String id = params.replace("guild_buff_level_", "");
            StringStore buffs = guild.getBuffs();
            return buffs.getValue(id,0)+"";
        }
        if (params.startsWith("guild_top_money_")) {
                List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.MONEY);
                int top = Integer.parseInt(params.replace("guild_top_money_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.startsWith("guild_top_members_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.MEMBERS);
                int top = Integer.parseInt(params.replace("guild_top_members_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.startsWith("guild_top_level_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.LEVEL);
                int top = Integer.parseInt(params.replace("guild_top_level_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.startsWith("guild_top_treelevel_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.TREELEVEL);
            int top = Integer.parseInt(params.replace("guild_top_treelevel_", ""));
            if (guilds.size() < top) {return lang.default_null;}
            return guilds.get(top).getGuild();
        }
        if (params.startsWith("guild_top_activity_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.ACTIVITY);
            int top = Integer.parseInt(params.replace("guild_top_activity_", ""));
            if (guilds.size() < top) {return lang.default_null;}
            return guilds.get(top).getGuild();
        }
        return "null";
    }

    private String getName(String arg) {
        if (arg != null && !arg.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            boolean b = false;
            char[] chars = arg.toCharArray();
            for (char c : chars) {
                if (b) {
                    if (c == '}') {
                        break;
                    }
                    builder.append(c);
                } else {
                    if (c == '{') {
                        b = true;
                    }
                }
            }
            return builder.toString();
        }
        return null;
    }
}
