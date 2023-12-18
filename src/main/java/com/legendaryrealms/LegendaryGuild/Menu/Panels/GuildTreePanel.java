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
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
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

                if (LegendaryGuild.getInstance().getWaterPotsManager().getWaterPot(menuItem.getValue()).isPresent()){
                    String pot = menuItem.getValue();
                    ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                            .addSinglePlaceHolder("use",waterDataStore.getAmount(pot, WaterDataStore.WaterDataType.TODAY)+"");
                    menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                }
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
                double next = LegendaryGuild.getInstance().getFileManager().getConfig().TREEEXP.getOrDefault(level, Double.valueOf(-1));
                GuildTreeLoader.GuildTreeIcon icon = loader.getTreeIcon();

                ItemStack i = icon.getPreviewItem(level).clone();
                ItemMeta id = i.getItemMeta();
                List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                id.setLore(lore);
                i.setItemMeta(id);

                ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                        .addSinglePlaceHolder("level",""+level)
                        .addSinglePlaceHolder("exp",""+guild.getTreeexp())
                        .addSinglePlaceHolder("bar",GuildAPI.getGuildTreeExpProgressBar(guild))
                        .addSinglePlaceHolder("next",next+"");
                i = replaceHolderUtils.startReplace(i,true,p.getName());

                List<Integer> slots = icon.getSlots(level);
                tree_slots = slots;

                ItemStack finalI = i;
                slots.forEach(s -> inv.setItem(s, finalI));
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
