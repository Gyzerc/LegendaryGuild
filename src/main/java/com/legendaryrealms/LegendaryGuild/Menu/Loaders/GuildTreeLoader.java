package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildTreeLoader extends MenuLoader {

    private GuildTreeIcon treeIcon;
    public GuildTreeLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/", "GuildTree.yml");
    }

    @Override
    protected void readDefault() {

        ConfigurationSection section = getSection("tree").orElse(null);
        HashMap<Integer, ItemStack> item = new HashMap<>();
        HashMap<Integer, List<Integer>> slots = new HashMap<>();

        if (section != null){
            for (String levelStr : section.getKeys(false)){
                item.put(Integer.parseInt(levelStr), ItemUtils.readItem(section,levelStr));
                slots.put(Integer.parseInt(levelStr),deserializeSlot(section.getString(levelStr+".slot","")));
            }
        }

        treeIcon = new GuildTreeIcon(item,slots);
    }

    public GuildTreeIcon getTreeIcon() {
        return treeIcon;
    }

    public class GuildTreeIcon {

        HashMap<Integer, ItemStack> item;
        HashMap<Integer, List<Integer>> slots;

        public GuildTreeIcon(HashMap<Integer, ItemStack> item, HashMap<Integer, List<Integer>> slots) {
            this.item = item;
            this.slots = slots;
        }

        public ItemStack getPreviewItem(int level) {
            return item.containsKey(level) ? item.get(level) : new ItemStack(Material.PAPER);
        }

        public List<Integer> getSlots(int level) {
            return slots.containsKey(level) ? slots.get(level) : new ArrayList<>();
        }
    }
}
