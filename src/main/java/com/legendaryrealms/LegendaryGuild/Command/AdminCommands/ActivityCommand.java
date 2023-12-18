package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ActivityCommand extends LegendaryCommand {
    public ActivityCommand() {
        super("legendaryguild.admin", "activity", 6, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {

        AddType type =getType(args[2]);
        String aot = args[3];
        String guildNameOrPlayerName = args[4];
        String amountStr = args[5];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            sender.sendMessage(lang.plugin+lang.notmath);
            return;
        }
        int amount = Integer.parseInt(amountStr);
        switch (type) {
            case GUILD: {
                if (!legendaryGuild.getGuildsManager().isExists(guildNameOrPlayerName)){
                    sender.sendMessage(lang.plugin+lang.notguild);
                    return;
                }
                Guild guild = legendaryGuild.getGuildsManager().getGuild(guildNameOrPlayerName);
                switch (aot.toLowerCase()){
                    case "add" : {
                        GuildAPI.addGuildActivity(null,guild,amount,AddType.GUILD);
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",amountStr));
                        return;
                    }
                    case "take" : {
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",GuildAPI.takeGuildActivity(null,guild,amount,AddType.GUILD)+""));
                        return;
                    }
                    case "set" : {
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",GuildAPI.setGuildActivity(null,guild,amount,AddType.GUILD)+""));
                        return;
                    }
                }
                return;
            }
            case PLAYER: {
                if (getPlayer(guildNameOrPlayerName) == null){
                    sender.sendMessage(lang.plugin+lang.notplayer);
                    return;
                }
                Player p = Bukkit.getPlayerExact(guildNameOrPlayerName);
                switch (aot.toLowerCase()){
                    case "add" : {
                        GuildAPI.addGuildActivity(p,null,amount,AddType.PLAYER);
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",amountStr));
                        return;
                    }
                    case "take" : {
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",GuildAPI.takeGuildActivity(p,null,amount,AddType.PLAYER)+""));
                        return;
                    }
                    case "set" : {
                        sender.sendMessage(lang.plugin+lang.admin_activity_add.replace("%target%",guildNameOrPlayerName).replace("%value%",GuildAPI.setGuildActivity(p,null,amount,AddType.PLAYER)+""));
                        return;
                    }
                }
                return;
            }
        }

    }

    private AddType getType(String str){
        try {
            return AddType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e){
            return AddType.GUILD;
        }
    }

    public enum AddType {
        GUILD,
        PLAYER;
    }
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("player","guild"),2,Arrays.asList("activity"),1)
                .addTab(Arrays.asList("add","take","set"),3,Arrays.asList("activity"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),4,Arrays.asList("guild"),2)
                .addTab(legendaryGuild.getOnlinePlayersName(),4,Arrays.asList("player"),2)
                .addTab(Arrays.asList("Amount(数量)"),5,Arrays.asList("add","take","set"),3)
                .build(args);
    }
}
