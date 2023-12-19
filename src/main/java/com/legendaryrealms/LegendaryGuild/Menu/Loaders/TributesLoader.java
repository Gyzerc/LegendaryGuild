package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TributesLoader extends MenuLoader {
    public TributesLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "Tributes.yml");
    }

    private ItemStack confirmItem;
    private List<Integer> confirmItem_slots;

    @Override
    protected void readDefault() {
        confirmItem = ItemUtils.readItem(yml,"sell_confirm");
        confirmItem_slots = deserializeSlot(getValue("sell_confirm.slot","[31]"));
    }

    public ItemStack getConfirmItem() {
        return confirmItem;
    }

    public List<Integer> getConfirmItem_slots() {
        return confirmItem_slots;
    }
}
