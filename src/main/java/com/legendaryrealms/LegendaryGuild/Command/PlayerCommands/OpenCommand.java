package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildMenuPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OpenCommand extends LegendaryCommand {
    public OpenCommand() {
        super("LegendaryGuild.open", "open", 1, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            User user = UserAPI.getUser(sender.getName());
            if (!user.hasGuild()){
                sender.sendMessage(lang.plugin+lang.nothasguild);
                return;
            }
            GuildMenuPanel guildMenuPanel = new GuildMenuPanel((Player) sender);
            guildMenuPanel.open();
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
