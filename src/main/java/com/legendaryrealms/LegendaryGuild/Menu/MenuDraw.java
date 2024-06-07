package com.legendaryrealms.LegendaryGuild.Menu;

import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.IntStore;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildMenuPanel;
import com.legendaryrealms.LegendaryGuild.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class MenuDraw implements InventoryHolder{


    public final Lang lang = LegendaryGuild.getInstance().getFileManager().getLang();
    public Inventory inv;
    private MenuLoader loader;
    public IntStore specialSlot;
    public Player p;
    public <T> MenuDraw(Player p,String menuId){
        this.p = p;
        this.loader = LegendaryGuild.getInstance().getMenuLoadersManager().getMenuLoader(menuId);
        this.specialSlot = new IntStore();
    }
    public Object getSlot(int slot){
      return specialSlot.getValue(slot,null);
    }
    public MenuItem getMenuItem(int slot){
        return loader.getMenuItem(slot);
    }
    public String getPlaceHolder(String id){
        return loader.getPlaceHolder(id);
    }
    public void DrawEssentail(Inventory inv){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                loader.getItem().forEach((integer, menuItem) -> {
                    inv.setItem(integer,menuItem.getI());
                });
            }
        });

    }
    public List<Integer> getLayout(){
        return loader.getLayout();
    }

    public void DrawEssentailSpecial(Inventory inv,Consumer<MenuItem> consumer){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                List<MenuItem> list = loader.getItem().values().stream().collect(Collectors.toList());
                for (MenuItem menuItem : list){
                    MenuItem newMenu = new MenuItem(menuItem.getId(),menuItem.getLayout(),menuItem.getI().clone(),menuItem.getFuction(),menuItem.getValue());
                    consumer.accept(newMenu);

                    ItemStack i = newMenu.getI();
                    ItemMeta id =i.getItemMeta();
                    List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                    lore = LegendaryGuild.getInstance().getHookManager().getPlaceholderAPIHook().replaceHolder(lore, Bukkit.getOfflinePlayer(p.getName()));

                    id.setLore(lore);
                    i.setItemMeta(id);

                    for (int slot : newMenu.getLayout()){
                        inv.setItem(slot,i);
                    }
                }
            }
        });
    }

    public boolean dealEssentailsButton(int slot){
        MenuItem fuction = loader.getMenuItem(slot);
        if (fuction != null){
            switch (fuction.getFuction()){
                case "close": {
                    p.closeInventory();
                    return true;
                }
                case "cmd": {
                    new RunUtils(Arrays.asList(fuction.getValue()),p).start();
                    return true;
                }
                case "back": {
                    GuildMenuPanel guildMenuPanel = new GuildMenuPanel(p);
                    guildMenuPanel.open();
                    return true;
                }
                case "none":
                    return true;
            }
        }
        return false;
    }

    public MenuLoader getLoader() {
        return loader;
    }



    public abstract void onClick(InventoryClickEvent e);

    public void open(){
        if (loader.getSound().isPresent()){
            p.playSound(p.getLocation(),loader.getSound().get(),1,1);
        }
        p.openInventory(inv);
    }

    public <T> boolean hasPage(int page, List<T> list){
        int start = 0 + (page-1) * getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        return list.size() > start ;
    }


    public <T> List<T> getPage(int page, List<T> list){
        int start = 0 + (page-1) * getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        List<T> rL = new ArrayList<>();
        for (int get = start;get < end ; get ++){
            if (list.size() > get) {
                rL.add(list.get(get));
            }
        }
        return rL;
    }


    public <T> List<T> getPage(int page,int perpage, List<T> list){
        int start = 0 + (page-1) * perpage;
        int end = perpage + (page-1)*perpage;
        List<T> rL = new ArrayList<>();
        for (int get = start;get < end ; get ++){
            if (list.size() > get) {
                rL.add(list.get(get));
            }
        }
        return rL;
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }
    public abstract void onDrag(InventoryDragEvent e);
    public void onClose(InventoryCloseEvent e) {

    }

}
