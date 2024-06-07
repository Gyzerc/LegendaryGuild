package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MenuCommand extends LegendaryCommand {
    public MenuCommand() {
        super("legendaryguild.menu", "menu", 2, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            User user = legendaryGuild.getUsersManager().getUser(sender.getName());
            String menuId = args[1];
            if (!menuId.equals("GuildList") && !user.hasGuild()){
                sender.sendMessage(lang.plugin+lang.nothasguild);
                return;
            }
            switch (menuId) {
                case "GuildList":
                    GuildListPanel guildListPanel = new GuildListPanel((Player) sender,1, GuildListPanel.Sort.DEFAULT);
                    guildListPanel.open();
                    break;
                case "Applications":
                    ApplicationsPanel applicationsPanel = new ApplicationsPanel((Player) sender,1);
                    applicationsPanel.loadPage();
                    applicationsPanel.open();
                    break;
                case "Members":
                    MembersPanel membersPanel = new MembersPanel((Player) sender,1, MembersPanel.Sort.POSITION);
                    membersPanel.loadPage();
                    membersPanel.open();
                    break;
                case "Stores":
                    if (legendaryGuild.getFileManager().getStores().isEnable()) {
                        StoresPanel storesPanel = new StoresPanel((Player) sender,1);
                        storesPanel.load();
                        storesPanel.open();
                    }
                    break;
                case "RedPackets":
                    RedPacketsPanel redPacketsPanel = new RedPacketsPanel((Player) sender,1);
                    redPacketsPanel.load();
                    redPacketsPanel.open();
                    break;
                case "GuildTree":
                    GuildTreePanel tree = new GuildTreePanel((Player) sender);
                    tree.open();
                    break;
                case "GuildIconsShop":
                    GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel((Player) sender,1);
                    guildIconsShopPanel.loadPage(1);
                    guildIconsShopPanel.open();
                    break;
                case "Tributes":
                    TributesPanel tributesPanel = new TributesPanel((Player) sender);
                    tributesPanel.open();
                    break;
                case "GuildShop":
                    GuildShopPanel guildShopPanel = new GuildShopPanel((Player) sender,1);
                    guildShopPanel.open();
                    break;
                case "Buff":
                    if (legendaryGuild.getBuffsManager().isEnable()){
                        BuffPanel buffPanel = new BuffPanel((Player) sender,1);
                        buffPanel.open();
                    }
                    break;
                case "ActivityRewards":
                    ActivityRewardsPanel rewardsPanel = new ActivityRewardsPanel((Player) sender);
                    rewardsPanel.open();
                    return;
                case "Positions":
                    PositionsPanel positionsPanel = new PositionsPanel((Player) sender);
                    positionsPanel.open();
                    return;
                case "TeamShop":
                    TeamShopPanel teamShopPanel = new TeamShopPanel((Player) sender,1);
                    teamShopPanel.open();
                    return;
                default:
                    sender.sendMessage(lang.plugin+lang.no_panel);
                    break;
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(legendaryGuild.getMenuLoadersManager().getMenuIds(),1,Arrays.asList("menu"),0)
                .build(args);
    }
}
