package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.logging.Level;

public class PlaceholderRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "placeholderapi";
    }

    @Override
    public boolean canPass(Player p, String str) {
        System.out.println("use papi");
        if (LegendaryGuild.getInstance().getHookManager().getPlaceholderAPIHook().isEnable()) {
            String[] args = str.split(";");
            if (args.length < 4) {
                LegendaryGuild.getInstance().info("配置出错！请输入数字数量 -> 参数不足： placeholderapi;变量名;判断符号;比较值;(可选)不通过发送的提示", Level.SEVERE);
                return false;
            }
            String input = args[1];
            String symbol = args[2];
            String value = args[3];

            Optional<String> msg = args.length == 5 ? Optional.of(LegendaryGuild.getInstance().color(args[4])) : Optional.empty();
            String output = PlaceholderAPI.setPlaceholders(p, input);
            switch (symbol) {
                case "=": {
                    if (output.equals(value)) {
                        return true;
                    }
                    break;
                }
                case ">": {
                    if (Double.parseDouble(output) > Double.parseDouble(value)) {
                        return true;
                    }
                    break;
                }
                case ">=": {
                    if (Double.parseDouble(output) >= Double.parseDouble(value)) {
                        return true;
                    }
                    break;
                }
                case "<": {
                    if (Double.parseDouble(output) < Double.parseDouble(value)) {
                        return true;
                    }
                    break;
                }
                case "<=": {
                    if (Double.parseDouble(output) <= Double.parseDouble(value)) {
                        return true;
                    }
                    break;
                }
            }
            msg.ifPresent(s -> p.sendMessage(s));
            return false;
        }
        LegendaryGuild.getInstance().info("&c检测到服务器未安装 PlaceholderAPI 该条 requirement 失效.. -> "+str,Level.SEVERE);
        return false;
    }

    @Override
    public void deal(Player p, String str) {

    }
}
