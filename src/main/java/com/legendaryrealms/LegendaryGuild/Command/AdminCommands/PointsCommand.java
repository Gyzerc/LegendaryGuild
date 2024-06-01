package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PointsCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public PointsCommand() {
        super("legendaryguild.admin", "points", 5, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String aot = args[2];
        String player = args[3];
        String amountStr = args[4];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            sender.sendMessage(lang.plugin+lang.notmath);
            return;
        }
        double amount = Double.parseDouble(amountStr);
        if (getPlayer(player) == null){
            sender.sendMessage(lang.plugin+lang.notplayer);
            return;
        }
        User user = legendaryGuild.getUsersManager().getUser(player);
        if (!user.hasGuild()){
            sender.sendMessage(lang.plugin+lang.admin_target_nothasguild);
            return;
        }
        boolean change=false;
        switch (aot.toLowerCase()){
            case "add" : {
                change = true;
                user.addPoints(amount,true);
                sender.sendMessage(lang.plugin+lang.admin_give_points.replace("%target%",player).replace("%value%",amountStr));
                break;
            }
            case "take" : {
                change = true;
                user.takePoints(amount,true);
                sender.sendMessage(lang.plugin+lang.admin_take_points.replace("%target%",player).replace("%value%",amountStr));
                break;
            }
            case "set" : {
                change = true;
                user.setPoints(amount,true);
                sender.sendMessage(lang.plugin+lang.admin_set_points.replace("%target%",player).replace("%value%",amountStr));
                break;
            }
        }

        if (change) {
            user.update(false);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","set","take"),2,Arrays.asList("points"),1)
                .addTab(legendaryGuild.getOnlinePlayersName(),3,Arrays.asList("add","take","set"),2)
                .addTab(Arrays.asList("Amount(数量)"),4,Arrays.asList("add","take","set"),2)
                .build(args);
    }

}
