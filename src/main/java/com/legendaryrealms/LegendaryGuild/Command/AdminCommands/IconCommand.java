package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildIcon;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IconCommand extends com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand {
    public IconCommand( ) {
        super("", "icon", 5, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String sym = args[2];
        Guild guild = GuildAPI.getGuild(args[3]).orElse(null);
        if (guild != null) {
            String id = args[4];
            GuildIcon icon = legendaryGuild.getGuildIconsManager().getIcon(id).orElse(null);
            if (icon != null) {
                List<String> icons = new ArrayList<>(guild.getUnlock_icons());
                switch (sym.toLowerCase()) {
                    case "add" : {
                        if (!icons.contains(id)) {
                            icons.add(id);
                            guild.setUnlock_icons(icons);
                            guild.update();
                        }
                        sender.sendMessage(lang.plugin + lang.admin_add_icon.replace("%target%", guild.getDisplay()).replace("%value%", icon.getDisplay()));
                        return;
                    }
                    case "remove" : {
                        icons.remove(id);
                        guild.setUnlock_icons(icons);
                        if (guild.getIcon().equals(id)) {
                            guild.setIcon("");
                        }
                        guild.update();
                        sender.sendMessage(lang.plugin + lang.admin_remove_icon.replace("%target%", guild.getDisplay()).replace("%value%", icon.getDisplay()));
                        return;
                    }
                }
            }
            sender.sendMessage(lang.plugin + lang.icon_not_exist);
            return;
        }
        sender.sendMessage(lang.plugin + lang.notguild);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","remove"),2,Arrays.asList("icon"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds() , 3 ,Arrays.asList("icon") , 1)
                .addTab(legendaryGuild.getGuildIconsManager().getIcons().stream().map(s -> s.getId()).collect(Collectors.toList()), 4 , Arrays.asList("icon") , 1)
                .build(args);
    }
}
