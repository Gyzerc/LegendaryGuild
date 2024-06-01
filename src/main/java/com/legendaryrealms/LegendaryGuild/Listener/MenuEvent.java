package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.StoresPanel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuEvent implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getInventory().getHolder() instanceof MenuDraw){
            MenuDraw draw = (MenuDraw) e.getInventory().getHolder();
            draw.onClick(e);
        }
    }

    @EventHandler
    public void onDraw(InventoryDragEvent e){
        if (e.getInventory().getHolder() instanceof MenuDraw){
            MenuDraw draw = (MenuDraw) e.getInventory().getHolder();
            draw.onDrag(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (e.getInventory().getHolder() instanceof MenuDraw ) {
            MenuDraw draw = (MenuDraw) e.getInventory().getHolder();
            draw.onClose(e);
        }
        if (e.getInventory().getHolder() instanceof StoresPanel.StoreContainer){
            StoresPanel.StoreContainer container = (StoresPanel.StoreContainer) e.getInventory().getHolder();
            GuildStore store = LegendaryGuild.getInstance().getDataBase().getStore(container.getGuild());
            store.setUse(container.getId(),"null");
            store.setContents(container.getId(),e.getInventory().getContents());
            //更新数据库
            LegendaryGuild.getInstance().getStoresManager().update(store);
        }
    }
}
