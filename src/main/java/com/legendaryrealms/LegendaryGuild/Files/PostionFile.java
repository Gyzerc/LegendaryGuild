package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Manager.User.PositionsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PostionFile extends FileProvider{

    private PositionsManager manager;
    public PostionFile(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild/Contents/config", "Contents/config/", "Position.yml");
    }

    @Override
    public void readDefault() {
        manager = legendaryGuild.getPositionsManager();

        String owner = getValue("owner","会长");
        String member = getValue("default","普通成员");

        Position ownerPos = new Position(owner,legendaryGuild.color(getValue("positions."+owner+".display","&6会长")),getValue("positions."+owner+".weight",0),1,getValue("positions."+owner+".attrs",new ArrayList<>()),true,true);
        manager.setOwnerPosition(ownerPos);

        Position memberPos = new Position(member,legendaryGuild.color(getValue("positions."+member+".display","&f普通成员")),getValue("positions."+member+".weight",99),-1,getValue("positions."+member+".attrs",new ArrayList<>()),false,false);
        manager.setDefaultPosition(memberPos);

        getSection("positions").ifPresent(configurationSection -> {
            for (String id : configurationSection.getKeys(false)){
                if (id.equals(owner) || id.equals(member)){
                    continue;
                }
                //展示名检测
                String display = configurationSection.getString(id+".display");
                if (display == null){
                    legendaryGuild.info("公会职位ID: "+id+" 缺少display,该职位不生效...", Level.SEVERE);
                    continue;
                }
                display = legendaryGuild.color(display);

                //权重检测
                if (configurationSection.get(id+".weight") == null){
                    legendaryGuild.info("公会职位ID: "+id+" 缺少weight,该职位不生效...", Level.SEVERE);
                    continue;
                }
                int weight = configurationSection.getInt(id+".weight");

                //最大人数检测
                int max = -1;
                if (configurationSection.get(id+".max") == null){
                    legendaryGuild.info("公会职位ID: "+id+" 缺少max,默认该职位最大人数为无限.", Level.SEVERE);
                }
                else {
                    max = configurationSection.getInt(id+".max");
                }

                boolean accept = configurationSection.getBoolean(id+".accept",false);
                boolean kick = configurationSection.getBoolean(id+".kick",false);

                List<String> attrs = configurationSection.getStringList(id+".attrs");
                manager.addPostion(id,new Position(id,display,weight,max,attrs,accept,kick));
            }
        });

        //最终检测
        manager.CanEnable();
    }
}
