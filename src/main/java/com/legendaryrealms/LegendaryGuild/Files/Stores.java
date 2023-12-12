package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.util.ArrayList;
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
            legendaryGuild.info("启用公会仓库模块",Level.INFO);
            return;
        }
        legendaryGuild.info("已关闭公会仓库模块.", Level.INFO);
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
}
