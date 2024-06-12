package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Stores extends FileProvider{
    public Stores(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config", "Contents/config/", "Stores.yml");
    }

    private boolean enable;
    private List<String> REQUIREMENTS;
    private int SIZE;
    private HashMap<Integer,Integer> LEVEL_TO_MAX_STORES;
    private String TITLE;
    private boolean limit_lore;
    private boolean limit_materials;
    private boolean limit_display;
    private List<String> lore;
    private List<Material> materials;
    private List<String> display;
    @Override
    protected void readDefault() {

        enable = getValue("enable",true);
        if (enable) {
            TITLE = legendaryGuild.color(getValue("settings.title","&e%id% &0号仓库"));
            LEVEL_TO_MAX_STORES = new HashMap<>();
            REQUIREMENTS = getValue("settings.unlock.requirements", new ArrayList<>());
            SIZE = getValue("settings.size", 54);
            getSection("settings.store_amount").ifPresent(sec -> {
                for (String levelStr : sec.getKeys(false)) {
                    int level = Integer.parseInt(levelStr);
                    LEVEL_TO_MAX_STORES.put(level,sec.getInt(levelStr));
                }
            });

            limit_lore = getValue("settings.limit.contains-lore.enable",true);
            limit_materials = getValue("settings.limit.materials.enable",true);
            limit_display = getValue("settings.limit.display.enable",true);

            lore = legendaryGuild.color(getValue("settings.limit.contains-lore.list", Arrays.asList("&c绑定")));
            display = legendaryGuild.color(getValue("settings.limit.display.list", Arrays.asList("&cTest Item")));
            materials = new ArrayList<>();
            for (String material :  getValue("settings.limit.materials.list" , Arrays.asList("BEDROCK"))) {
                Material id = Material.getMaterial(material.toUpperCase());
                if (id != null) {
                    materials.add(id);
                }
            }


            legendaryGuild.info("启用公会仓库模块 & Enabled Guild Stores.",Level.INFO);
            return;
        }
        legendaryGuild.info("已关闭公会仓库模块. & Disabled Guild Stores.", Level.INFO);
    }

    public boolean isEnable() {
        return enable;
    }

    public List<String> getREQUIREMENTS() {
        return REQUIREMENTS;
    }

    public int getSIZE() {
        return SIZE;
    }

    public HashMap<Integer, Integer> getLEVEL_TO_MAX_STORES() {
        return LEVEL_TO_MAX_STORES;
    }

    public String getTITLE() {
        return TITLE;
    }

    public boolean isLimit_lore() {
        return limit_lore;
    }

    public boolean isLimit_materials() {
        return limit_materials;
    }

    public boolean isLimit_display() {
        return limit_display;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public List<String> getDisplay() {
        return display;
    }
}
