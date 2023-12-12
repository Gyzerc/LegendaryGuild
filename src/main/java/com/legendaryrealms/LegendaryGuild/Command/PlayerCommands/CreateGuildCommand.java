package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CreateGuildCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public CreateGuildCommand() {
        super("legendaryguild.create", "create", 2, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            String guildName = args[1];
            User user = LegendaryGuild.getInstance().getUsersManager().getUser(sender.getName());
            GuildAPI.createGuild(user,guildName);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("公会名称 (Guild Name)"),1,Arrays.asList("create"),0)
                .build(args);
    }
}
