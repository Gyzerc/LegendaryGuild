package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerPointsRequirement extends Requirement {
    private String SYMBOL = "playerpoints";
    public boolean canPass(Player p, String str){
        if (!legendaryGuild.getHookManager().getPlayerPointsHook().isEnable()){
            legendaryGuild.info("检测到服务器未安装 PlayerPoints .",Level.SEVERE);
            legendaryGuild.info("Please install PlayerPoints to use 'playerpoints;XXX'.",Level.SEVERE);
            return false;
        }
        try {
            String[] args=str.split(";");
            double price = Double.parseDouble(args[1]);
            if (legendaryGuild.getHookManager().getPlayerPointsHook().getPlayerPoints().getAPI().look(p.getUniqueId()) >= price){
                return true;
            }
            p.sendMessage(lang.plugin+lang.reuirement_notenough_playerpoints.replace("%value%",price+""));
            return false;
        } catch (ClassCastException e){
            legendaryGuild.info("出现错误 -> "+str +" 值必须为double类型！", Level.SEVERE);
            return false;
        } catch (NullPointerException e){
            legendaryGuild.info("出现错误 -> "+str +" 缺少参数！请确认写法正确！", Level.SEVERE);
            return false;
        }
    }

    @Override
    public void deal(Player p, String str) {
        if (!legendaryGuild.getHookManager().getPlayerPointsHook().isEnable()){
            return;
        }
        try {
            String[] args=str.split(";");
            double price = Double.parseDouble(args[1]);
            legendaryGuild.getHookManager().getPlayerPointsHook().getPlayerPoints().getAPI().take(p.getUniqueId(), (int) price);
            return;
        } catch (ClassCastException e){
            legendaryGuild.info("出现错误 -> "+str +" 值必须为double类型！", Level.SEVERE);
            return;
        } catch (NullPointerException e){
            legendaryGuild.info("出现错误 -> "+str +" 缺少参数！请确认写法正确！", Level.SEVERE);
            return;
        }
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }
}
