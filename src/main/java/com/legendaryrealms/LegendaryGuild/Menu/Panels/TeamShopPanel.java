package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildTeamShopData;
import com.legendaryrealms.LegendaryGuild.Data.Others.TeamShopItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.TeamShopLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
import com.legendaryrealms.LegendaryGuild.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamShopPanel extends MenuDraw {
    private TeamShopLoader teamShopLoader;
    private int page;
    private List<String> list;
    private List<String> currentPage;
    public TeamShopPanel(Player p,int page) {
        super(p, "TeamShop");
        this.page = page;
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        this.teamShopLoader = (TeamShopLoader) getLoader();

        Guild guild = UserAPI.getGuild(p.getName()).get();
        GuildTeamShopData teamShopData = guild.getGuildTeamShopData();
        TeamShopItem shopItem = LegendaryGuild.getInstance().getTeamShopManager().getShopItem(teamShopData.getTodayShopId());
        if (shopItem == null) {
            teamShopData.randomShop();
            shopItem = LegendaryGuild.getInstance().getTeamShopManager().getShopItem(teamShopData.getTodayShopId());
        }
        list = getBargainsLore(teamShopData.getBargains(),guild);
        currentPage = getPage(page,teamShopLoader.getShowPerPage(),list);
        TeamShopItem finalShopItem = shopItem;

        DrawEssentailSpecial(inv, menuItem -> {
            ItemStack i = menuItem.getI().clone();
            switch (menuItem.getFuction()) {
                case "bargains" : {
                    int index = 0;

                    ItemMeta id = i.getItemMeta();
                    List<String> lore =  new ArrayList<>();
                    for (String l : (List<String>)(id.hasLore() ? id.getLore() : new ArrayList<>())) {
                        if (l.contains("%placeholder_bargains%")) {
                            if (currentPage.size() > index) {
                                lore.add(currentPage.get(index));
                                index++;
                            }
                            continue;
                        }
                        lore.add(l);
                    }
                    id.setLore(lore);
                    i.setItemMeta(id);

                    ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                            .addSinglePlaceHolder("bargain",String.valueOf(teamShopData.getBargains().size()))
                            .addSinglePlaceHolder("members",String.valueOf(guild.getMembers().size()));

                    menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    return;
                }
                case "item" : {
                    ItemStack preview = finalShopItem.getPreview().clone();
                    i = new ReplaceHolderUtils().addListPlaceHolder("lore" , preview.getItemMeta().getLore()).startReplace(i,false,null);

                    String placeholder_whether_bargain_already = teamShopData.hasBargain(p.getName()) ?
                            getPlaceHolder("whether-bargain-already") : getPlaceHolder("whether_bargain-wait");
                    String placeholder_limit = finalShopItem.getLimit() > 0 ?
                            getPlaceHolder("limit").replace("%purchased%",String.valueOf(teamShopData.getBuyAmount(p.getName()))).replace("%limit%",String.valueOf(finalShopItem.getLimit())) :
                            "";

                    ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                    .addSinglePlaceHolder("display",preview.getItemMeta().getDisplayName())
                                    .addSinglePlaceHolder("placeholder_whether_bargain",placeholder_whether_bargain_already)
                                    .addSinglePlaceHolder("bargain", String.valueOf(teamShopData.getBargains().size()))
                                    .addSinglePlaceHolder("members", String.valueOf(guild.getMembers().size()))
                                    .addSinglePlaceHolder("current_price",String.valueOf(teamShopData.getCurrentPrice()))
                                    .addSinglePlaceHolder("placeholder_limit",placeholder_limit);

                    menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                    return;
                }
            }
        });
    }

    private List<String> getBargainsLore(HashMap<String, Double> bargains, Guild guild) {
        List<String> notBargainMembers = guild.getMembers().stream().filter(name -> !bargains.containsKey(name)).collect(Collectors.toList());
        List<String> lore = new ArrayList<>();
        String bargained = getPlaceHolder("bargains-already");
        String wait = getPlaceHolder("bargains-not");
        bargains.forEach((player,bargain) -> {
            lore.add(bargained.replace("%player%" , player).replace("%bargain-price%",String.valueOf(bargain)));
        });
        notBargainMembers.forEach(player -> lore.add(wait.replace("%player%",player)));
        return lore;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()) {
                    case "item" : {
                        if (e.isLeftClick()) {
                            Guild guild = UserAPI.getGuild(p.getName()).get();
                            GuildTeamShopData teamShopData = guild.getGuildTeamShopData();
                            if (teamShopData.hasBargain(p.getName())) {
                                p.sendMessage(lang.plugin + lang.bargain_already);
                                return;
                            }
                            teamShopData.bargain(guild,p.getName());
                            p.sendMessage(lang.plugin + lang.bargain_success.replace("%current%",String.valueOf(teamShopData.getCurrentPrice())).replace("%bargain%",String.valueOf(teamShopData.getBargainPrice(p.getName()))));
                            LegendaryGuild.getInstance().getMsgUtils().sendGuildMessage(guild.getMembers(),lang.plugin + lang.bargain_bargain_broad.replace("%player%",p.getName()).replace("%current%",String.valueOf(teamShopData.getCurrentPrice())).replace("%bargain%",String.valueOf(teamShopData.getBargainPrice(p.getName()))));
                            TeamShopPanel teamShopPanel = new TeamShopPanel(p,page);
                            teamShopPanel.open();
                            return;
                        }
                        if (e.isRightClick()) {
                            User user = UserAPI.getUser(p.getName());
                            Guild guild = UserAPI.getGuild(p.getName()).get();
                            GuildTeamShopData teamShopData = guild.getGuildTeamShopData();
                            TeamShopItem shopItem = LegendaryGuild.getInstance().getTeamShopManager().getShopItem(teamShopData.getTodayShopId());

                            if (UserAPI.buyGuildTeamShop(p,user,guild,shopItem,teamShopData)) {
                                new TeamShopPanel(p, page).open();
                            }
                            return;
                        }
                        return;
                    }
                    case "bargains" : {

                        if (e.isRightClick()) {
                            if (page > 1) {
                                new TeamShopPanel(p,(page-1)).open();
                            }
                            return;
                        }
                        if (e.isLeftClick()) {

                            if (!getPage((page + 1) , teamShopLoader.getShowPerPage() , list).isEmpty()) {
                                new TeamShopPanel(p, (page+1)).open();
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}
