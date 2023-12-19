package com.legendaryrealms.LegendaryGuild.API;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
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
        return "5.0.2";
    }

    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private final Lang lang = legendaryGuild.getFileManager().getLang();
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        User user = UserAPI.getUser(player.getName());
        Guild guild = UserAPI.getGuild(player.getName()).orElse(null);
        if (params.equals("guild"))
            return guild != null ? guild.getGuild() :  lang.default_guild;
        if (params.equals("position")) {
            Position position = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition());
            return position.getDisplay();
        }
        if (params.equals("points"))
            return user.getPoints() + "";
        if (params.equals("points_total"))
            return user.getTotal_points() + "";
        if (params.equals("guild_members"))
            return (guild != null) ? (guild.getMembers().size() + "") : "0";
        if (params.equals("guild_maxmembers")) {
            int max = legendaryGuild.getFileManager().getConfig().MEMBERS.getOrDefault(guild.getLevel(),guild.getMembers().size());
            return ""+max;
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
        if (params.equals("activity")) {
            GuildActivityData data = legendaryGuild.getGuildActivityDataManager().getData(guild.getGuild());
            return (guild != null) ? (data.getPoints() + "") : "0.0";
        }
        if (params.contains("player_pot_day_")) {
            String id = params.replace("player_pot_day_", "");
            WaterDataStore waterDataStore = user.getWaterDataStore();
            return ""+waterDataStore.getAmount(id, WaterDataStore.WaterDataType.TODAY);
        }
        if (params.contains("player_pot_total_")) {
            String id = params.replace("player_pot_total_", "");
            WaterDataStore waterDataStore = user.getWaterDataStore();
            return ""+waterDataStore.getAmount(id, WaterDataStore.WaterDataType.TOTAL);
        }
        if (params.contains("guild_buff_level_")) {
            String id = params.replace("guild_buff_level_", "");
            StringStore buffs = guild.getBuffs();
            return buffs.getValue(id,0)+"";
        }
        if (params.contains("guild_top_money_")) {
                List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.MONEY);
                int top = Integer.parseInt(params.replace("guild_top_money_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.contains("guild_top_members_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.MEMBERS);
                int top = Integer.parseInt(params.replace("guild_top_members_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.contains("guild_top_level_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.LEVEL);
                int top = Integer.parseInt(params.replace("guild_top_level_", ""));
                if (guilds.size() < top) {return lang.default_null;}
                return guilds.get(top).getGuild();
        }
        if (params.contains("guild_top_treelevel_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.TREELEVEL);
            int top = Integer.parseInt(params.replace("guild_top_treelevel_", ""));
            if (guilds.size() < top) {return lang.default_null;}
            return guilds.get(top).getGuild();
        }
        if (params.contains("guild_top_activity_")) {
            List<Guild> guilds = legendaryGuild.getGuildsManager().getGuildsBy(GuildListPanel.Sort.ACTIVITY);
            int top = Integer.parseInt(params.replace("guild_top_activity_", ""));
            if (guilds.size() < top) {return lang.default_null;}
            return guilds.get(top).getGuild();
        }
        return "null";
    }
}
