package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeamShopLoader extends MenuLoader {
    public TeamShopLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "TeamShop.yml");
    }

    private int showPerPage;
    @Override
    protected void readDefault() {

    }

    @Override
    protected void readSpecials() {
        getItem().forEach((integer, menuItem) -> {
            if (menuItem.getFuction().equals("bargains")) {
                ItemStack i = menuItem.getI();
                ItemMeta id = i.getItemMeta();
                List<String> lore = id.hasLore() ? id.getLore() : new ArrayList<>();
                lore.forEach(l -> {
                    if (l.contains("%placeholder_bargains%")) {
                        showPerPage ++;
                    }
                });
            }
        });
    }

    public int getShowPerPage() {
        return showPerPage;
    }
}
