package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.logging.Level;

public class ChanceRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "chance";
    }

    @Override
    public boolean canPass(Player p, String str) {
        String[] args = str.split(";");
        if (!legendaryGuild.checkIsNumber(args[1])){
            legendaryGuild.info("请输入正确的数字！ ->"+str, Level.SEVERE);
            return false;
        }
        double chance =  Double.parseDouble(args[1]);
        if ((new Random()).nextInt(101) <= (chance*100)){
            return true;
        }
        p.sendMessage(lang.plugin+lang.nopass_chance);
        return false;
    }

    @Override
    public void deal(Player p, String str) {

    }
}
