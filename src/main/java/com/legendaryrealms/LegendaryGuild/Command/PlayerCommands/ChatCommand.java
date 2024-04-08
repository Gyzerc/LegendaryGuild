package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatCommand extends LegendaryCommand {
    public ChatCommand() {
        super("LegendaryGuild.chat", "chat", 1, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            User user = UserAPI.getUser(sender.getName());
            if (!user.hasGuild()) {
                sender.sendMessage(lang.plugin+lang.nothasguild);
                return;
            }
            if (user.isChat()) {
                user.setChat(false);
                sender.sendMessage(lang.plugin+lang.chat_disable);
                return;
            }
            user.setChat(true);
            sender.sendMessage(lang.plugin+lang.chat_enable);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
