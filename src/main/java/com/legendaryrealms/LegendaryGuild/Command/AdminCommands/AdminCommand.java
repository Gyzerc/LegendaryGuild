package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public AdminCommand() {
        super("legendaryguild.admin", "admin", 1, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        lang.help_admin.forEach(msg -> {
            sender.sendMessage(msg);
        });
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
