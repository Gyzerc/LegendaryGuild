package com.legendaryrealms.LegendaryGuild.Utils;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.AdminCommands.ActivityCommand;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class RunUtils {

    private List<String> runs;
    private Player p;

    public RunUtils(List<String> runs, Player p) {
        this.runs = runs;
        this.p = p;
    }

    public void start() {
        runs.forEach(s -> {
            deal(s);
        });
    }


    private void deal(String run){
        if (run.startsWith("[")){
            String tag = getSymbol(run);
            String dealStr = LegendaryGuild.getInstance().color(run.replace("["+tag+"]","").replace("%player%",p.getName()));
            double chance = getChance(dealStr);
            if (chance != -1){
                dealStr=dealStr.replace("~chance:"+chance,"");
            }
            chance = ((chance==-1) ? 1 : chance);
            switch (tag) {
                case "player":
                    if ((new Random()).nextInt(101) <= chance*100){
                        p.performCommand(dealStr);
                    }
                    break;
                case "op":
                    if ((new Random()).nextInt(101) <= chance*100){
                        if (p.isOp()){
                            p.performCommand(dealStr);
                            break;
                        }
                        p.setOp(true);
                        p.performCommand(dealStr);
                        p.setOp(false);
                    }
                    break;
                case "console":
                    if ((new Random()).nextInt(101) <= chance*100){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),dealStr);
                    }
                    break;
                case "message":
                    if ((new Random()).nextInt(101) <= chance*100){
                        p.sendMessage(dealStr);
                    }
                    break;
                case "title":
                    if ((new Random()).nextInt(101) <= chance*100){
                        String[] title = dealStr.split(";");
                        String main = getFromArray(title,0,"");
                        String sub = getFromArray(title,1,"");
                        p.sendTitle(main,sub);
                    }
                    break;
                case "sound":
                    if ((new Random()).nextInt(101) <= chance*100){
                        String[] args = dealStr.split(";");
                        String sound = getFromArray(args,0,"BLOCK_CHEST_OPEN");
                        int pitch = getFromArray(args,1,1);
                        int volume = getFromArray(args,2,1);
                        p.playSound(p.getLocation(), Sound.valueOf(sound),pitch,volume);
                    }
                    break;
                case "broad":
                    if ((new Random()).nextInt(101) <= chance*100){
                        LegendaryGuild.getInstance().getMsgUtils().sendBroad(dealStr);
                    }
                    break;
                case "broad_guild":
                    if ((new Random()).nextInt(101) <= chance*100){
                        User user = UserAPI.getUser(p.getName());
                        if (user.hasGuild()){
                            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                            LegendaryGuild.getInstance().getMsgUtils().sendGuildMessage(guild.getMembers(),dealStr);
                        }
                    }
                    break;
                case "guild_money":
                    if ((new Random()).nextInt(101) <= chance*100){
                        User user = UserAPI.getUser(p.getName());
                        if (user.hasGuild()) {
                            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                            double amount = Double.parseDouble(dealStr);
                            guild.addMoney(amount);
                            guild.update();
                        }
                    }
                    break;
                case "guild_points":
                    if ((new Random()).nextInt(101) <= chance*100) {
                        User user = UserAPI.getUser(p.getName());
                        if (user.hasGuild()) {
                            user.addPoints(Double.parseDouble(dealStr),true);
                            user.update(false);
                        }
                    }
                    break;
                case "guild_activity":
                    if ((new Random()).nextInt(101) <= chance*100) {
                        User user = UserAPI.getUser(p.getName());
                        if (user.hasGuild()) {
                            GuildAPI.addGuildActivity(p,null,Double.parseDouble(dealStr), ActivityCommand.AddType.PLAYER);
                        }
                    }
                    break;
                case "guild_exp" :
                    User user = UserAPI.getUser(p.getName());
                    if (user.hasGuild()) {
                        if (UserAPI.getGuild(p.getName()).isPresent()) {
                            GuildAPI.addGuildExp(p.getName(), UserAPI.getGuild(p.getName()).get(), Double.parseDouble(dealStr));
                        }
                    }
            }
            return;
        }
        LegendaryGuild.getInstance().info("配置格式出错:必须以[标识]开头。比如[console]xxxx -> "+run, Level.SEVERE);
    }



    private <T> T getFromArray(String[] str,int pos,T def){
        if (str != null && str.length > pos) {
            return (T)str[pos];
        }
        return def;
    }
    private String getSymbol(String str){
        StringBuilder builder = new StringBuilder();
        boolean begin = false;
        for (char c : str.toCharArray()){
            if (begin){
                if (c == ']'){
                    break;
                }
                builder.append(c);
            }
            else {
                if (c == '['){
                    begin = true;
                }
            }
        }
        return builder.toString();
    }

    private double getChance(String str){
        if (str.contains("~chance:")){
            String chanceStr = str.substring(str.lastIndexOf(":")+1);
            return Double.parseDouble(chanceStr);
        }
        return -1.0;
    }
}
