package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildListPanel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends LegendaryCommand {
    public ListCommand() {
        super("LegendaryGuild.list", "list", 1, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            GuildListPanel listPanel = new GuildListPanel((Player) sender,1, GuildListPanel.Sort.DEFAULT);
            listPanel.open();
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
