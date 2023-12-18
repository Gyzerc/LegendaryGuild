package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class JoinCommand extends LegendaryCommand {
    public JoinCommand() {
        super("LegendaryGuild.join", "join", 2, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            String guildName = args[1];
            UserAPI.sendApplication(UserAPI.getUser(sender.getName()),guildName);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),1,Arrays.asList("join"),0)
                .build(args);
    }
}
