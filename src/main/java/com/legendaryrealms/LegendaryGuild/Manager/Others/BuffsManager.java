package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.IntStore;
import com.legendaryrealms.LegendaryGuild.Hook.Attribute.AttributePluginProvider;
import com.legendaryrealms.LegendaryGuild.Hook.Attribute.AttributePlus3;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

public class BuffsManager {
    private boolean enable = false;
    private LinkedHashMap<String, Buff> cache;
    private LegendaryGuild legendaryGuild;
    private AttributePluginProvider.AttributePlugin plugin;
    private AttributePluginProvider provider;
    public BuffsManager(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        this.cache = new LinkedHashMap<>();
        this.plugin = AttributePluginProvider.AttributePlugin.valueOf(legendaryGuild.getFileManager().getBuffFile().getValue("plugin","AP3"));
        if (AttributePluginProvider.HookPlugin(plugin,this)) {
            readBuffs();
            enable = true;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public Optional<Buff> getBuff(String id){
        return cache.containsKey(id) ? Optional.of(cache.get(id)) : Optional.empty();
    }
    private void readBuffs() {
        int a = 0;
        ConfigurationSection section = legendaryGuild.getFileManager().getBuffFile().getSection("buffs").orElse(null);
        if (section != null){
            for (String id : section.getKeys(false)){
                String display = legendaryGuild.color(section.getString(id+".display","未命名的Buff"));
                int max = section.getInt(id+".max",5);
                IntStore<List<String>> requirements = new IntStore<>();
                IntStore<ItemStack> preview = new IntStore<>();
                IntStore<List<String>> attr = new IntStore<>();
                ConfigurationSection upgrades = section.getConfigurationSection(id+".upgrade");
                if (upgrades != null){
                    for (String levelStr : upgrades.getKeys(false)){
                        int level = Integer.parseInt( levelStr);
                        requirements.setValue(level,upgrades.getStringList(levelStr+".requirements"),new ArrayList<>());
                        preview.setValue(level, ItemUtils.readItem(upgrades,levelStr+".preview"),null);
                        attr.setValue(level,upgrades.getStringList(levelStr+".attr"),new ArrayList<>());
                    }
                }
                cache.put(id,new Buff(id,display,max,preview,requirements,attr));
                a++;
            }
        }
        legendaryGuild.info("加载 "+a+" 个Buff. & Load "+a+" Guild Buffs.",Level.INFO);
    }
    public AttributePluginProvider.AttributePlugin getPlugin() {
        return plugin;
    }
    public LinkedList<Buff> getBuffs(){
        return new LinkedList<>(cache.values());
    }

    public void hook(AttributePluginProvider attributePluginProvider) {
        provider = attributePluginProvider;
    }

    public AttributePluginProvider getProvider() {
        return provider;
    }
}
