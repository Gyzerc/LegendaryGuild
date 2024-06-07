package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeamShopCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public TeamShopCommand( ) {
        super("", "teamshop", 4, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String guildName = args[3];
        Optional<Guild> optionalGuild = GuildAPI.getGuild(guildName);
        if (optionalGuild.isPresent()) {
            Guild guild = optionalGuild.get();
            GuildAPI.refreshGuildTeamShopItem(guild);
            sender.sendMessage(lang.plugin + lang.admin_teamshop_refresh.replace("%target%",guild.getDisplay()));
            return;
        }
        sender.sendMessage(lang.plugin + lang.notguild);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("refresh") , 2 , Arrays.asList("teamshop") , 1)
                .addTab(LegendaryGuild.getInstance().getGuildsManager().getGuilds() , 3 , Arrays.asList("refresh") , 2)
                .build(args);
    }
}
