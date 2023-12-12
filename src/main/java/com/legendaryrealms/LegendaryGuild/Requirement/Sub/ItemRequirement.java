package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ItemRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "item";
    }

    @Override
    public boolean canPass(Player p, String str) {
        String[] args = str.split(";");
        String materialStr = args[1];
        Material material = Material.getMaterial(materialStr);
        if (material == null){
            legendaryGuild.info("配置出错！没有该ID -> "+str, Level.SEVERE);
            return false;
        }
        String dataStr = args[2];
        String amountStr = args[3];
        if (!legendaryGuild.checkIsNumber(dataStr) ||  !legendaryGuild.checkIsNumber(amountStr)){
            legendaryGuild.info("配置出错！请输入数字DATA、数量 -> "+str, Level.SEVERE);
            return false;
        }
        String display = materialStr;
        if (args.length == 5){
            display = legendaryGuild.color(args[4]);
        }
        int data = Integer.parseInt(dataStr);
        int amount = Integer.parseInt(amountStr);
        if ( ItemUtils.hasPlayerMcItem(p,material,data) < amount ){
            p.sendMessage(lang.plugin+lang.requirement_notenough_item.replace("%item%",display).replace("%value%",amountStr));
            return false;
        }
        return true;
    }

    @Override
    public void deal(Player p, String str) {
        String[] args = str.split(";");
        String materialStr = args[1];
        Material material = Material.getMaterial(materialStr);
        if (material == null){
            legendaryGuild.info("配置出错！没有该ID -> "+str, Level.SEVERE);
            return;
        }
        String dataStr = args[2];
        String amountStr = args[3];
        if (!legendaryGuild.checkIsNumber(dataStr) ||  !legendaryGuild.checkIsNumber(amountStr)){
            legendaryGuild.info("配置出错！请输入数字DATA、数量 -> "+str, Level.SEVERE);
            return;
        }
        int data = Integer.parseInt(dataStr);
        int amount = Integer.parseInt(amountStr);
        ItemUtils.TakePlayerMcItem(p,material,data,amount);
    }
}
