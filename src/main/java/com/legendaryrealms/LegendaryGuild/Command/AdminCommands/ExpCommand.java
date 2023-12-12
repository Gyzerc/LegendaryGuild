package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ExpCommand extends LegendaryCommand {
    public ExpCommand() {
        super("legendaryguild.admin", "exp", 5, true);
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
        int amount = Integer.parseInt(amountStr);
        if (!legendaryGuild.getGuildsManager().isExists(guildName)){
            sender.sendMessage(lang.plugin+lang.notguild);
            return;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);
        switch (aot.toLowerCase()){
            case "add" : {
                GuildAPI.addGuildExp(sender.getName(),guild,amount);
                sender.sendMessage(lang.plugin+lang.admin_exp_add.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }
            case "take" : {
                GuildAPI.takeGuildExp(guild,amount);
                sender.sendMessage(lang.plugin+lang.admin_exp_take.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }
            case "set" : {
                GuildAPI.setGuildExp(guild,amount);
                sender.sendMessage(lang.plugin+lang.admin_exp_set.replace("%target%",guildName).replace("%value%",amountStr));
                break;
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","set","take"),2,Arrays.asList("exp"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),3,Arrays.asList("add","take","set"),2)
                .addTab(Arrays.asList("Amount(数量)"),4,Arrays.asList("add","take","set"),2)
                .build(args);
    }
}
