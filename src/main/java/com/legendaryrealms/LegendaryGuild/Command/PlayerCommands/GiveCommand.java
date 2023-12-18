package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GiveCommand extends LegendaryCommand {
    public GiveCommand() {
        super("LegendaryGuild.give", "give", 2, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            UserAPI.giveGuild((Player) sender,args[1]);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("公会成员 (Guild Member)"),1,Arrays.asList("give"),0)
                .build(args);
    }
}
