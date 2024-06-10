package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildIcon;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuildMenuPanel extends MenuDraw {
    public GuildMenuPanel(Player p) {
        super(p, "GuildMenu");
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        User user = UserAPI.getUser(p.getName());
        Guild guild = UserAPI.getGuild(p.getName()).orElse(null);

        if (guild != null) {
            DrawEssentailSpecial(inv, menuItem -> {
                switch (menuItem.getFuction()) {
                    case "notice" : {
                        ItemStack i = menuItem.getI();
                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils();
                        menuItem.setI( replaceHolderUtils.addListPlaceHolder("notice",guild.getNotice())
                                .startReplace(i,true,p.getName()));
                        return;
                    }
                    case "information": {
                        ItemStack i = menuItem.getI();
                        ItemMeta id = i.getItemMeta();

                        GuildIcon icon = LegendaryGuild.getInstance().getGuildIconsManager().getIcon(guild.getIcon()).orElse(null);
                        if (icon != null){
                            ItemStack item = new ItemStack(icon.getMaterial(),1,(short) icon.getData());
                            ItemMeta itemid = item.getItemMeta();
                            if (id.hasDisplayName()){
                                itemid.setDisplayName(id.getDisplayName());
                            }
                            if (LegendaryGuild.getInstance().version_high){
                                itemid.setCustomModelData(icon.getModel());
                            }
                            if (id.hasLore()){
                                itemid.setLore(id.getLore());
                            }
                            item.setItemMeta(itemid);

                            i = item;
                        }

                        double next = LegendaryGuild.getInstance().getFileManager().getConfig().EXP.getOrDefault(guild.getLevel(), Double.valueOf(-1));
                        double treenext = LegendaryGuild.getInstance().getFileManager().getConfig().TREEEXP.getOrDefault(guild.getTreelevel(), Double.valueOf(-1));
                        int maxmembers = guild.getMaxMembers();
                        GuildActivityData activityData = LegendaryGuild.getInstance().getGuildActivityDataManager().getData(guild.getGuild());
                        ReplaceHolderUtils replace = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("activity",activityData.getPoints()+"")
                                .addSinglePlaceHolder("total_activity",activityData.getTotal_points()+"")
                                .addSinglePlaceHolder("owner",guild.getOwner())
                                .addSinglePlaceHolder("level",""+guild.getLevel())
                                .addSinglePlaceHolder("exp",""+guild.getExp())
                                .addSinglePlaceHolder("next",""+next)
                                .addSinglePlaceHolder("treelevel",""+guild.getTreelevel())
                                .addSinglePlaceHolder("treeexp",""+guild.getTreeexp())
                                .addSinglePlaceHolder("treenext",""+treenext)
                                .addSinglePlaceHolder("money",""+guild.getMoney())
                                .addSinglePlaceHolder("members",""+guild.getMembers().size())
                                .addSinglePlaceHolder("maxmembers",""+maxmembers)
                                .addSinglePlaceHolder("date",guild.getDate())
                                .addListPlaceHolder("intro",guild.getIntro());
                        menuItem.setI(replace.startReplace(i,true,p.getName()));
                        return;
                    }
                    case "tree" : {
                        ItemStack i = menuItem.getI();
                        double treenext = LegendaryGuild.getInstance().getFileManager().getConfig().TREEEXP.getOrDefault(guild.getTreelevel(), Double.valueOf(-1));
                        ReplaceHolderUtils replace = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("level",guild.getTreelevel()+"")
                                .addSinglePlaceHolder("exp",guild.getTreeexp()+"")
                                .addSinglePlaceHolder("next",""+treenext)
                                .addSinglePlaceHolder("bar",GuildAPI.getGuildTreeExpProgressBar(guild));
                        menuItem.setI(replace.startReplace(i,true,p.getName()));
                        return;
                    }
                    case "shop" : {
                        ItemStack i = menuItem.getI();
                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("points",user.getPoints()+"")
                                .addSinglePlaceHolder("total_points",user.getTotal_points()+"");
                        menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    }
                    case "positions" : {
                        ItemStack i = menuItem.getI();
                        Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(user.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("position",position.getDisplay());
                        menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    }
                    case "members" : {
                        ItemStack i = menuItem.getI();
                        int maxmembers = guild.getMaxMembers();
                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("members",guild.getMembers().size()+"")
                                .addSinglePlaceHolder("maxmembers",maxmembers+"");
                        menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    }
                    case "applications" : {
                        ItemStack i = menuItem.getI();
                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("amount",guild.getApplications().size()+"");
                        menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    }
                }
            });
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                User user = UserAPI.getUser(p.getName());
                Guild guild = UserAPI.getGuild(p.getName()).orElse(null);

                if (guild != null) {
                    switch (menuItem.getFuction()) {
                        case "tree" : {
                            GuildTreePanel treePanel = new GuildTreePanel(p);
                            treePanel.open();
                            return;
                        }
                        case "shop" : {
                            GuildShopPanel shopPanel = new GuildShopPanel(p,1);
                            shopPanel.open();
                            return;
                        }
                        case "tributes" : {
                            TributesPanel tributesPanel = new TributesPanel(p);
                            tributesPanel.open();
                            return;
                        }
                        case "redpackets" : {
                            RedPacketsPanel redPacketsPanel = new RedPacketsPanel(p,1);
                            redPacketsPanel.open();
                            return;
                        }
                        case "stores": {
                            if (LegendaryGuild.getInstance().getFileManager().getStores().isEnable()) {
                                StoresPanel storesPanel = new StoresPanel(p, 1);
                                storesPanel.open();
                            }
                            return;
                        }
                        case "buff": {
                            if (LegendaryGuild.getInstance().getBuffsManager().isEnable()) {
                                BuffPanel buffPanel = new BuffPanel(p, 1);
                                buffPanel.open();
                            }
                            return;
                        }
                        case "positions" : {
                            PositionsPanel positionsPanel = new PositionsPanel(p);
                            positionsPanel.open();
                            return;
                        }
                        case "members" : {
                            MembersPanel membersPanel = new MembersPanel(p,1, MembersPanel.Sort.POSITION);
                            membersPanel.open();
                            return;
                        }
                        case "applications": {
                            ApplicationsPanel applicationsPanel = new ApplicationsPanel(p, 1);
                            applicationsPanel.open();
                            return;
                        }
                        case "activity": {
                            ActivityRewardsPanel activityRewardsPanel = new ActivityRewardsPanel(p);
                            activityRewardsPanel.open();
                            return;
                        }
                        case "icon" : {
                            GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel(p,1);
                            guildIconsShopPanel.open();
                            return;
                        }
                        case "pvp" : {
                            p.performCommand("legendaryguild pvp");
                            return;
                        }
                        case "chat" : {
                            p.performCommand("legendaryguild chat");
                            return;
                        }
                        case "teamshop" : {
                            TeamShopPanel teamShopPanel = new TeamShopPanel(p,1);
                            teamShopPanel.open();
                            return;
                        }
                        case "home": {
                            if (e.isLeftClick()) {
                                UserAPI.teleportGuildHome(p);
                                p.closeInventory();
                                return;
                            }
                            if (e.isRightClick()) {
                                UserAPI.setGuildHome(p);
                                p.closeInventory();
                                return;
                            }
                            break;
                        }
                        case "information": {
                            if (guild.getOwner().equals(p.getName())) {
                                if (e.isLeftClick()) {
                                    p.closeInventory();
                                    LegendaryGuild.getInstance().getChatControl().setModify(p.getUniqueId(),0);
                                    p.sendMessage(lang.plugin+lang.input);
//                                    new AnvilGUI.Builder().title(lang.input)
//                                            .text("...")
//                                            .plugin(LegendaryGuild.getInstance())
//                                            .onClick((slot, stateSnapshot) -> {
//                                                String target = LegendaryGuild.getInstance().color(stateSnapshot.getText());
//                                                if(slot != AnvilGUI.Slot.OUTPUT || target.equals("...")) {
//                                                    return Collections.emptyList();
//                                                }
//
//                                                List<String> intro = guild.getIntro();
//                                                intro.add(target);
//
//                                                guild.setIntro(intro);
//                                                guild.update();
//
//                                                p.sendMessage(lang.plugin+lang.intro_add.replace("%value%",target));
//                                                return Arrays.asList(AnvilGUI.ResponseAction.close(),
//                                                        AnvilGUI.ResponseAction.run(() -> {
//                                                            GuildMenuPanel menuPanel = new GuildMenuPanel(p);
//                                                            menuPanel.open();
//                                                        }));
//                                            })
//                                            .open(p);
                                    return;
                                }
                                if (e.isRightClick()){
                                    p.closeInventory();
                                    List<String> intro = new ArrayList<>(guild.getIntro());
                                    if (intro.size() > 0){
                                        String remove = intro.remove(intro.size()-1);
                                        guild.setIntro(intro);
                                        guild.update();

                                        p.sendMessage(lang.plugin+lang.intro_remove.replace("%value%",LegendaryGuild.getInstance().color(remove)));
                                        GuildMenuPanel menuPanel = new GuildMenuPanel(p);
                                        menuPanel.open();
                                        return;
                                    }
                                }
                                return;
                            }
                            p.sendMessage(lang.plugin+lang.notowner);
                            return;
                        }
                        case "notice": {
                            if (guild.getOwner().equals(p.getName())) {
                                if (e.isLeftClick()) {
                                    p.closeInventory();
                                    LegendaryGuild.getInstance().getChatControl().setModify(p.getUniqueId(),1);
                                    p.sendMessage(lang.plugin+lang.input);
//                                    new AnvilGUI.Builder().title(lang.input)
//                                            .text("...")
//                                            .plugin(LegendaryGuild.getInstance())
//                                            .onClick((slot, stateSnapshot) -> {
//                                                String target = LegendaryGuild.getInstance().color(stateSnapshot.getText());
//                                                if(slot != AnvilGUI.Slot.OUTPUT || target.equals("...")) {
//                                                    return Collections.emptyList();
//                                                }
//
//                                                List<String> notice = guild.getNotice();
//                                                notice.add(target);
//
//                                                guild.setIntro(notice);
//                                                guild.update();
//
//                                                p.sendMessage(lang.plugin+lang.notice_add.replace("%value%",target));
//                                                return Arrays.asList(AnvilGUI.ResponseAction.close(),
//                                                        AnvilGUI.ResponseAction.run(() -> {
//                                                            GuildMenuPanel menuPanel = new GuildMenuPanel(p);
//                                                            menuPanel.open();
//                                                        }));
//                                            })
//                                            .open(p);
                                    return;
                                }
                                if (e.isRightClick()){
                                    List<String> notice = new ArrayList<>(guild.getNotice());
                                    if (notice.size() > 0){
                                        String remove = notice.remove(notice.size()-1);
                                        guild.setNotice(notice);
                                        guild.update();

                                        p.sendMessage(lang.plugin+lang.notice_remove.replace("%value%",LegendaryGuild.getInstance().color(remove)));
                                        GuildMenuPanel menuPanel = new GuildMenuPanel(p);
                                        menuPanel.open();
                                        return;
                                    }
                                }
                                return;
                            }
                            p.sendMessage(lang.plugin+lang.notowner);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
}
