package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.entity.Player;

public class ActivityRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "activity";
    }

    @Override
    public boolean canPass(Player p, String str) {
        User user = UserAPI.getUser(p.getName());
        String[] args=str.split(";");
        double price = Double.parseDouble(args[1]);
        if (user.hasGuild()) {
            GuildActivityData guildActivityData = legendaryGuild.getGuildActivityDataManager().getData(user.getGuild());
            return guildActivityData.getPlayerActivity(p.getName()) >= price;
        }
        p.sendMessage(lang.plugin + lang.requirement_notenough_activity.replace("%value%",args[1]));
        return false;
    }

    @Override
    public void deal(Player p, String str) {
    }
}
