package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.Events.GuildTributesEvent;
import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.TributeItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.TributesLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TributesPanel extends MenuDraw {
    private TributesLoader tributesLoader;
    public TributesPanel(Player p) {
        super(p, "Tributes");
        this.tributesLoader = (TributesLoader) getLoader();
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        DrawEssentail(inv);

        ItemStack i = tributesLoader.getConfirmItem().clone();
        ItemMeta id = i.getItemMeta();
        List<String> lore = id.getLore();
        lore.replaceAll( l -> l.replace("%points%","0.0").replace("%exp%","0.0"));
        id.setLore(lore);
        i.setItemMeta(id);
        for (int slot : tributesLoader.getConfirmItem_slots()){
            inv.setItem(slot,i);
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if (e.isShiftClick()) {
            e.setCancelled(true);
        }
        if (dealEssentailsButton(e.getRawSlot())) {
            e.setCancelled(true);
            return;
        }
        else {
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null){
                e.setCancelled(true);
                return;
            }
            if (tributesLoader.getConfirmItem_slots().contains(e.getRawSlot())){
                e.setCancelled(true);
                sell(e.getInventory());
                return;
            }
            if (getLayout().contains(e.getRawSlot())  ){
                Bukkit.getScheduler().runTaskLater(LegendaryGuild.getInstance(),()->{
                    updataInv(e.getInventory());
                },3);
            }
        }
    }

    public void updataInv(Inventory inventory)
    {
        double points=0.0;
        double exp=0.0;
        for (int slot: getLayout())
        {
            ItemStack i=inventory.getItem(slot);
            if (i != null)
            {
                Optional<TributeItem> tributesId= LegendaryGuild.getInstance().getTributesItemsManager().getTributeItem(i);
                if (tributesId.isPresent())
                {
                    TributeItem tributes= tributesId.get();
                    points+=tributes.getPoints() * i.getAmount();
                    exp+=tributes.getExp() * i.getAmount();
                }
            }
        }

        double finalPoints = points;
        double finalExp = exp;

        ItemStack i=tributesLoader.getConfirmItem().clone();
        ItemMeta id=i.getItemMeta();
        List<String> lore=id.hasLore() ? id.getLore() : new ArrayList<>();
        lore.replaceAll(x -> x.replace("%points%",""+ finalPoints).replace("%exp%",""+ finalExp));
        id.setLore(lore);
        i.setItemMeta(id);
        for (int slot: tributesLoader.getConfirmItem_slots())
        {
            inventory.setItem(slot,i);
        }

    }

    @Override
    public void onClose(InventoryCloseEvent e){
        for (int slot : getLayout()) {
            ItemStack i = e.getInventory().getItem(slot);
            if (i != null && !i.getType().equals(Material.AIR)) {
                ItemUtils.giveItem(p,i,i.getAmount(),false);
            }
        }
    }
    public void sell(Inventory inventory)
    {
        User user = UserAPI.getUser(p.getName());
        Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
        HashMap<TributeItem,Integer> broads = new HashMap<>();
        List<TributeItem> tributeItems = new ArrayList<>();

        double points=0.0;
        double exp=0.0;
        for (int slot: getLayout())
        {
            ItemStack i=inventory.getItem(slot);
            if (i != null)
            {
                Optional<TributeItem> tributesId= LegendaryGuild.getInstance().getTributesItemsManager().getTributeItem(i);
                if (tributesId.isPresent())
                {
                    int amount = i.getAmount();
                    TributeItem tributes= tributesId.get();
                    points+=tributes.getPoints()*amount;
                    exp+=tributes.getExp()*amount;
                    i.setAmount(0);
                    if (tributes.isBroad()){
                        broads = addValue(broads,tributes,amount);
                    }
                    if (!tributeItems.contains(tributes)){tributeItems.add(tributes);}
                }
            }
        }

        if (points != 0 || exp != 0) {
            user.addPoints(points, true);
            user.update(false);
            GuildAPI.addGuildExp(p.getName(), guild, exp);

            Bukkit.getPluginManager().callEvent(new GuildTributesEvent(p,tributeItems));
        }
        if (!broads.isEmpty()){
            for (TributeItem tributeItem : broads.keySet()){
                LegendaryGuild.getInstance().getMsgUtils().sendGuildMessage(guild.getMembers(),tributeItem.getMessage().replace("%player%",p.getName()).replace("%amount%",""+broads.get(tributeItem)));
            }
        }

        ItemStack i=tributesLoader.getConfirmItem().clone();
        ItemMeta id=i.getItemMeta();
        List<String> lore=id.hasLore() ? id.getLore() : new ArrayList<>();
        lore.replaceAll(x -> x.replace("%points%",""+ 0).replace("%exp%",""+ 0));
        id.setLore(lore);
        i.setItemMeta(id);
        for (int slot: tributesLoader.getConfirmItem_slots())
        {
            inventory.setItem(slot,i);
        }
    }

    private HashMap<TributeItem,Integer> addValue(HashMap<TributeItem,Integer> broads,TributeItem tributeItem,int amount){
        HashMap<TributeItem,Integer> returnMap = broads;
        if (returnMap.containsKey(tributeItem)){
            int old = returnMap.get(tributeItem);
            returnMap.put(tributeItem,(old+amount));
        }
        else {
            returnMap.put(tributeItem,amount);
        }
        return returnMap;
    }

    @Override
    public void onDrag(InventoryDragEvent e) {
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                updataInv(e.getInventory());
            }
        },5);
    }
}
