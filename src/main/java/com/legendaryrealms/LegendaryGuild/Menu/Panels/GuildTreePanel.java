package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.GuildTreeLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuildTreePanel extends MenuDraw {

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
    private List<Integer> tree_slots ;
    public GuildTreePanel(Player p) {
        super(p, "GuildTree");
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());
        DrawEssentailSpecial(inv,menuItem -> {
            if (menuItem.getFuction().equals("pot")){
                WaterDataStore waterDataStore = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName()).getWaterDataStore();
                ItemStack i = menuItem.getI();
                ItemMeta id = i.getItemMeta();
                List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                if (LegendaryGuild.getInstance().getWaterPotsManager().getWaterPot(menuItem.getValue()).isPresent()){
                    String pot = menuItem.getValue();
                    lore.replaceAll( l -> l.replace("%use%",waterDataStore.getAmount(pot, WaterDataStore.WaterDataType.TODAY)+""));
                }
                id.setLore(lore);
                i.setItemMeta(id);
                menuItem.setI(i);
            }
        });
        loadPanel();
    }
    private void loadPanel(){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                GuildTreeLoader loader = (GuildTreeLoader) getLoader();
                User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
                Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                int level = guild.getTreelevel();
                GuildTreeLoader.GuildTreeIcon icon = loader.getTreeIcon();
                ItemStack i = icon.getPreviewItem(level).clone();
                ItemMeta id = i.getItemMeta();
                List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                lore.replaceAll(l -> l.replace("%level%",""+level)
                        .replace("%exp%",""+guild.getTreeexp())
                        .replace("%bar%", GuildAPI.getGuildTreeExpProgressBar(guild))
                        .replace("%next%",""+LegendaryGuild.getInstance().getFileManager().getConfig().TREEEXP.get(level)));
                id.setLore(lore);
                i.setItemMeta(id);
                List<Integer> slots = icon.getSlots(level);
                tree_slots = slots;
                slots.forEach(s -> inv.setItem(s,i));
            }
        });
    }

    public List<Integer> getTree_slots() {
        return tree_slots;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GuildTreeLoader loader = (GuildTreeLoader) getLoader();
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()) {
                    case "wish":
                        if (UserAPI.GuildTreeWish(UserAPI.getUser(p.getName()))){
                            p.closeInventory();
                        }
                        break;
                    case "pot":
                        String potId = menuItem.getValue();
                        if (UserAPI.GuildTreeWater(p,potId)){
                            GuildTreePanel tree = new GuildTreePanel(p);
                            tree.open();
                        }
                        break;
                }
            }
            else {
                if (tree_slots.contains(e.getRawSlot())){
                    if (GuildAPI.addGuildTreeLevelByPlayer(p)){
                        GuildTreePanel tree = new GuildTreePanel(p);
                        tree.open();
                    }
                }
            }
        }
    }
}
