package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class GuildMoneyRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "guild_money";
    }

    @Override
    public boolean canPass(Player p, String str) {
        String[] args = str.split(";");
        if (!legendaryGuild.checkIsNumber(args[1])){
            legendaryGuild.info("请输入正确的数字！ ->"+str, Level.SEVERE);
            return false;
        }
        User user = legendaryGuild.getUsersManager().getUser(p.getName());
        if (!user.hasGuild()){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return false;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        if (guild.getMoney() >= Double.parseDouble(args[1])){
            return true;
        }
        p.sendMessage(lang.plugin+lang.no_guildmoney.replace("%value%",args[1]));
        return false;
    }

    @Override
    public void deal(Player p, String str) {
        String[] args = str.split(";");
        User user = legendaryGuild.getUsersManager().getUser(p.getName());
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        guild.takeMoney(Double.parseDouble(args[1]));

        //更新数据库
        guild.update();
    }
}
