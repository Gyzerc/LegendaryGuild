package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildIcon;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.GuildIconsShopLoader;
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
import java.util.HashMap;
import java.util.List;


public class GuildIconsShopPanel extends MenuDraw {
    private int page;
    public GuildIconsShopPanel(Player p, int page) {
        super(p, "GuildIconsShop");
        this.page = page;
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        DrawEssentail(inv);
        loadPage(page);
    }
    private HashMap<Integer,GuildIcon> slot_toicon;

    public void loadPage(int page){
        this.slot_toicon =  new HashMap<>();
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                User user = UserAPI.getUser(p.getName());
                Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                List<GuildIcon> list=  LegendaryGuild.getInstance().getGuildIconsManager().getIcons();
                GuildIconsShopLoader loader = (GuildIconsShopLoader) getLoader();
                if (hasPage(page,list)){
                    int a = 0;

                    String locked = loader.getPlaceHolder("locked");
                    String unlocked = loader.getPlaceHolder("unlocked");
                    String putting = loader.getPlaceHolder("putting");

                    for (GuildIcon guildIcon : getPage(page,list)){
                        String current = locked;
                        if (guild.getIcon().equals(guildIcon.getId())){
                            current=putting;
                        }
                        else if (guild.getUnlock_icons().contains(guildIcon.getId())){
                            current=unlocked;
                        }

                        ItemStack i = new ItemStack(guildIcon.getMaterial(),1,(short) guildIcon.getData());
                        ItemMeta id = i.getItemMeta();
                        id.setDisplayName(loader.getIcon_display());
                        List<String> lore = new ArrayList<>(loader.getIcon_lore());

                        id.setLore(lore);
                        if (LegendaryGuild.getInstance().version_high) {
                            id.setCustomModelData(guildIcon.getModel());
                        }
                        i.setItemMeta(id);


                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addListPlaceHolder("description",guildIcon.getDescription())
                                .addSinglePlaceHolder("icon",guildIcon.getDisplay())
                                .addSinglePlaceHolder("placeholder",current);


                        inv.setItem(getLayout().get(a), replaceHolderUtils.startReplace(i,false,null));
                        slot_toicon.put(getLayout().get(a),guildIcon);
                        a++;
                    }
                }
            }
        });
    }


    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GuildIconsShopLoader loader = (GuildIconsShopLoader) getLoader();
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()) {
                    case "pre": {
                        if (page <= 1) {
                            return;
                        }
                        GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel(p, (page - 1));
                        guildIconsShopPanel.loadPage((page - 1));
                        guildIconsShopPanel.open();
                        break;
                    }
                    case "next": {
                        if (getPage(page + 1,LegendaryGuild.getInstance().getGuildIconsManager().getIcons()).isEmpty()) {
                            return;
                        }
                        GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel(p, (page + 1));
                        guildIconsShopPanel.loadPage((page + 1));
                        guildIconsShopPanel.open();
                        break;
                    }
                }
            }
            else {
                GuildIcon icon = slot_toicon.get(e.getRawSlot());
                if (icon != null){
                    User user = UserAPI.getUser(p.getName());
                    Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                    if (e.isLeftClick()) {
                        if (!guild.getIcon().equals(icon.getId())){
                            if (guild.getUnlock_icons().contains(icon.getId())){
                                guild.setIcon(icon.getId());
                                guild.update();

                                p.sendMessage(lang.plugin+lang.icon_put.replace("%value%",icon.getDisplay()));

                                GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel(p, page);
                                guildIconsShopPanel.loadPage(page);
                                guildIconsShopPanel.open();
                                return;
                            }
                            p.sendMessage(lang.plugin+lang.icon_locked);
                            return;
                        }
                        return;
                    }
                    if (e.isRightClick()) {
                        if (guild.getUnlock_icons().contains(icon.getId())){
                         return;
                        }
                        if (LegendaryGuild.getInstance().getRequirementsManager().check(p,icon.getRequirements())){
                            LegendaryGuild.getInstance().getRequirementsManager().deal(p,icon.getRequirements());

                            List<String> icons = new ArrayList<>(guild.getUnlock_icons());
                            icons.add(icon.getId());
                            guild.setUnlock_icons(icons);
                            guild.update();

                            p.sendMessage(lang.plugin+lang.icon_unlock.replace("%value%",icon.getDisplay()));

                            GuildIconsShopPanel guildIconsShopPanel = new GuildIconsShopPanel(p, page);
                            guildIconsShopPanel.loadPage(page);
                            guildIconsShopPanel.open();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }
    @Override
    public void onDrag(InventoryDragEvent e){

    }
}
