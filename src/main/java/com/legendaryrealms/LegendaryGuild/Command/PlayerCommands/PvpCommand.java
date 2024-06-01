package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PvpCommand extends LegendaryCommand {
    public PvpCommand() {
        super("LegendaryGuild.pvp", "pvp", 1, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (UserAPI.hasGuild(sender.getName())){
                User user = UserAPI.getUser(sender.getName());
                switch (user.getPvp()){
                    case ALL:
                        user.setPvp(User.PvpType.NO_SAME_GUILD);
                        user.update(false);

                        sender.sendMessage(lang.plugin+lang.pvp_enable);
                        break;
                    default:
                        user.setPvp(User.PvpType.ALL);
                        user.update(false);

                        sender.sendMessage(lang.plugin+lang.pvp_disable);
                        break;
                }
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
