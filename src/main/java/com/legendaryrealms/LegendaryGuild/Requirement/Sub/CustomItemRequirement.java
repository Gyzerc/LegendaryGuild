package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CustomItemRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "customitem";
    }

    @Override
    public boolean canPass(Player p, String str) {
        String[] args = str.split(";");
        String display = legendaryGuild.color(args[1]);
        String amountStr = args[2];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            legendaryGuild.info("配置出错！请输入数字数量 -> "+str, Level.SEVERE);
            return false;
        }
        int amount = Integer.parseInt(amountStr);
        if ( ItemUtils.hasPlayerItem(p,display) < amount ){
            p.sendMessage(lang.plugin+lang.requirement_notenough_item.replace("%item%",display).replace("%value%",amountStr));
            return false;
        }
        return true;
    }

    @Override
    public void deal(Player p, String str) {
        String[] args = str.split(";");
        String display = legendaryGuild.color(args[1]);
        String amountStr = args[2];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            legendaryGuild.info("配置出错！请输入数字数量 -> "+str, Level.SEVERE);
            return;
        }
        int amount = Integer.parseInt(amountStr);
        ItemUtils.TakePlayerItem(p,display,amount);
    }
}
