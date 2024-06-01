package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.IntStore;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.BuffLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
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

public class BuffPanel extends MenuDraw {
    private int page;
    public BuffPanel(Player p, int page) {
        super(p, "Buff");
        this.page = page;
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        DrawEssentail(inv);
        loadPage(page);
    }



    private void loadPage(int page) {
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                LinkedList<Buff> buffs = LegendaryGuild.getInstance().getBuffsManager().getBuffs();
                if (hasPage(page,buffs)){
                    int a = 0;
                    User user = UserAPI.getUser(p.getName());
                    Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                    StringStore data = guild.getBuffs();
                    for (Buff buff: getPage(page,buffs)){
                        String buffId = buff.getId();
                        ItemStack i = buff.getPreview(Integer.parseInt(data.getValue(buffId,0).toString())).clone();
                        ItemMeta id = i.getItemMeta();
                        String name = id.hasDisplayName() ? id.getDisplayName() : "";
                        id.setDisplayName(replaceHolder(name,buff,data));
                        List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                        lore.replaceAll( l -> replaceHolder(l,buff,data));
                        id.setLore(lore);
                        i.setItemMeta(id);
                        inv.setItem(getLayout().get(a), i);
                        specialSlot.setValue(getLayout().get(a), buff,null);
                        a++;
                    }
                }
            }
        });
    }


    private String replaceHolder(String str,Buff buff,StringStore data){
        return str.replace("%buff_display%",buff.getDisplay()).replace("%buff_maxlevel%",buff.getMax()+"").replace("%buff_level%",String.valueOf(data.getValue(buff.getId(),0)));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        BuffLoader loader = (BuffLoader) getLoader();
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()) {
                    case "pre": {
                        if (page <= 1) {
                            return;
                        }
                        BuffPanel buffPanel = new BuffPanel(p, (page - 1));
                        buffPanel.loadPage(page-1);
                        buffPanel.open();
                        break;
                    }
                    case "next": {
                        LinkedList<Buff> buffs = LegendaryGuild.getInstance().getBuffsManager().getBuffs();
                        if (!hasPage(page + 1,buffs)) {
                            return;
                        }
                        BuffPanel buffPanel = new BuffPanel(p, (page + 1));
                        buffPanel.loadPage(page+1);
                        buffPanel.open();
                        break;
                    }
                }
            } else {
                Buff buff = specialSlot.getValue(e.getRawSlot(), null) != null ? (Buff) specialSlot.getValue(e.getRawSlot(), null):null;
                if (buff != null) {
                    User user = UserAPI.getUser(p.getName());
                    Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                    if (guild.getOwner().equals(p.getName())) {
                        if (GuildAPI.addGuildBuffLevel(guild, p, buff)) {
                            BuffPanel panel = new BuffPanel(p, page);
                            panel.open();
                        }
                        return;
                    }
                    p.sendMessage(lang.plugin+lang.notowner);
                    return;
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
}
