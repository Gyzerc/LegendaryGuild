package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MoneyCommand extends LegendaryCommand {

    public MoneyCommand() {
        super("legendaryguild.admin", "money", 5, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String aot = args[2];
        String guildName = args[3];
        String amountStr = args[4];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            sender.sendMessage(lang.plugin+lang.notmath);
            return;
        }
        double amount = Double.parseDouble(amountStr);
        if (!legendaryGuild.getGuildsManager().isExists(guildName)){
            sender.sendMessage(lang.plugin+lang.notguild);
            return;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);
        boolean change = false;
        switch (aot.toLowerCase()){
            case "add" : {
                change = true;
                guild.addMoney(amount);
                sender.sendMessage(lang.plugin+lang.admin_give_money.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }
            case "take" : {
                change = true;
                guild.takeMoney(amount);
                sender.sendMessage(lang.plugin+lang.admin_take_money.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }
            case "set" : {
                change = true;
                guild.setMoney(amount);
                sender.sendMessage(lang.plugin+lang.admin_set_money.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }

        }

        if (change) {
            //更新数据库
            guild.update();
        }

    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","set","take"),2,Arrays.asList("money"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),3,Arrays.asList("add","take","set"),2)
                .addTab(Arrays.asList("Amount(数量)"),4,Arrays.asList("add","take","set"),2)
                .build(args);
    }
}
