package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MaxMembersCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public MaxMembersCommand( ) {
        super("", "maxmembers", 5, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String guildName = args[3];
        String amountStt = args[4];
        Optional<Guild> guildOptional = GuildAPI.getGuild(guildName);
        if (guildOptional.isPresent()) {
            if (legendaryGuild.checkIsNumber(amountStt)) {
                Guild guild = guildOptional.get();
                int amount = Math.max(0,Integer.parseInt(amountStt));
                switch (args[2].toLowerCase()) {
                    case "add": {
                        int a = guild.getExtra_members();
                        guild.setExtra_members(a + amount);
                        guild.update();

                        sender.sendMessage(lang.plugin + lang.admin_maxmembers_add.replace("%target%",guild.getDisplay()).replace("%value%",String.valueOf(amount)));
                        return;
                    }
                    case "set" : {
                        guild.setExtra_members(amount);
                        guild.update();

                        sender.sendMessage(lang.plugin + lang.admin_maxmembers_set.replace("%target%",guild.getDisplay()).replace("%value%",String.valueOf(amount)));
                        return;
                    }
                }
                return;
            }
            sender.sendMessage(lang.plugin + lang.notmath);
            return;
        }
        sender.sendMessage(lang.plugin + lang.notguild);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","set"),2,Arrays.asList("maxmembers"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),3,Arrays.asList("add","set"),2)
                .addTab(Arrays.asList("Amount(数量)"),4,Arrays.asList("add","set"),2)
                .build(args);
    }
}
