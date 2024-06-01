package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.Events.GuildShopBuyEvent;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ItemBuyData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopItem;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.GuildShopLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
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
import java.util.LinkedList;
import java.util.List;

public class GuildShopPanel extends MenuDraw {
    private int page;
    public GuildShopPanel(Player p,int page) {
        super(p, "GuildShop");
        this.page = page;
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        this.slot_item = new HashMap<>();
        DrawEssentail(inv);
        loadShops(page);
    }

    private HashMap<Integer,ShopItem> slot_item;
    public void loadShops(int page){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                LinkedList<ShopItem> items = LegendaryGuild.getInstance().getGuildShopItemsManager().getItems();
                if (hasPage(page,items)){
                    User user = UserAPI.getUser(p.getName());
                    GuildShopData shopData = LegendaryGuild.getInstance().getGuildShopDataManager().getData();

                    int a = 0;
                    for (ShopItem shopItem : getPage(page,items)){

                        ItemBuyData data = shopData.getData(shopItem.getId()).get();
                        String limit = "";
                        if (!shopItem.getType().equals(ShopType.UNLIMITED)){

                            limit = getLoader().findPlaceholderIgnore(shopItem.getType().name())
                                    .replace("%left%",""+data.getBuyAmount(p.getName(),shopItem.getType()))
                                    .replace("%max%",""+shopItem.getLimitAmount());
                        }

                        ItemStack i = shopItem.getDisplay().clone();
                        ItemMeta id = i.getItemMeta();
                        List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                        id.setLore(lore);
                        i.setItemMeta(id);

                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("points",String.valueOf(user.getPoints()))
                                        .addSinglePlaceHolder("limit",limit);

                        inv.setItem(getLayout().get(a), replaceHolderUtils.startReplace(i,true,p.getName()));
                        slot_item.put(getLayout().get(a), shopItem);
                        a++;
                    }
                }
            }
        });
    }

    private ShopItem getShopItem(int slot){
        return slot_item.get(slot);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())){
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null){
                switch (menuItem.getFuction()){
                    case "pre" : {
                        if (page <= 1) {
                            return;
                        }
                        GuildShopPanel shopPanel = new GuildShopPanel(p, (page - 1));
                        shopPanel.open();
                        break;
                    }
                    case "next": {
                        LinkedList<ShopItem> items = LegendaryGuild.getInstance().getGuildShopItemsManager().getItems();
                        if (hasPage(page + 1, items)) {
                            GuildShopPanel shopPanel = new GuildShopPanel(p, (page + 1));
                            shopPanel.open();
                        }
                        break;
                    }
                }
            }
            else {
                ShopItem shopItem = getShopItem(e.getRawSlot());
                if (shopItem != null) {
                    GuildShopData guildShopData = LegendaryGuild.getInstance().getGuildShopDataManager().getData();
                    ItemBuyData buyData = guildShopData.getData(shopItem.getId()).get();

                    boolean reachMax = false;
                    if (!shopItem.getType().equals(ShopType.UNLIMITED)) {
                        ShopType type = shopItem.getType();
                        int max = shopItem.getLimitAmount();

                        int hasBuy = buyData.getBuyAmount(p.getName(), type);
                        reachMax = (hasBuy >= max);
                    }

                    if (LegendaryGuild.getInstance().getRequirementsManager().check(p, shopItem.getRequirements())) {
                        if (reachMax) {
                            p.sendMessage(lang.plugin + lang.shop_limit);
                            return;
                        }
                        LegendaryGuild.getInstance().getRequirementsManager().deal(p, shopItem.getRequirements());

                        RunUtils runUtils = new RunUtils(shopItem.getRuns(), p);
                        Bukkit.getScheduler().runTask(LegendaryGuild.getInstance(), () -> runUtils.start());

                        //更新限购数据
                        buyData.addBuyAmount(p.getName(), shopItem.getType(), 1);
                        LegendaryGuild.getInstance().getGuildShopDataManager().updateData(guildShopData);

                        String name = e.getCurrentItem().getItemMeta().hasDisplayName() ? e.getCurrentItem().getItemMeta().getDisplayName() : e.getCurrentItem().getType().name();
                        p.sendMessage(lang.plugin + lang.shop_buy.replace("%value%", name));

                        Bukkit.getPluginManager().callEvent(new GuildShopBuyEvent(p,shopItem));

                        GuildShopPanel shopPanel = new GuildShopPanel(p, page);
                        shopPanel.open();
                        return;
                    }

                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
}
