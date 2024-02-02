package com.legendaryrealms.LegendaryGuild.Manager.Guild;

import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Data.Guild.CreateGuildSection;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


import java.util.*;
import java.util.logging.Level;

public class CreateGroupsManager {
    private LegendaryGuild legendaryGuild;
    private HashMap<String, CreateGuildSection> cache_byId;
    private Optional<CreateGuildSection> defaultSection;
    private LinkedList<CreateGuildSection> sort_weight;
    public CreateGroupsManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        cache_byId = new HashMap<>();
        //默认组
        defaultSection = Optional.empty();

        //读取所有组
        readGroups();

        //排序
        sort_weight=sortSections();
    }

    public Optional<CreateGuildSection> getPlayerGroup(Player p){
        if (defaultSection.isPresent()) {
            for (int get = sort_weight.size()-1 ; get >= 0;get --){
                CreateGuildSection section = sort_weight.get(get);
                if (p.hasPermission(section.getPerm())) {
                    return Optional.of(section);
                }
            }
            return defaultSection;
        }
        return Optional.empty();
    }

    public boolean checkGroup(Player p,CreateGuildSection section){
        if (section != null) {
            List<String> requirements = section.getRequirements();
            for (String str : requirements) {
                Requirement requirement = legendaryGuild.getRequirementsManager().serialize(str).orElse(null);
                if (requirement != null) {
                    if (!requirement.canPass(p, str)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void dealGroup(Player p,CreateGuildSection section){
        if (section != null) {
            List<String> requirements = section.getRequirements();
            for (String str : requirements) {
                Requirement requirement = legendaryGuild.getRequirementsManager().serialize(str).orElse(null);
                requirement.deal(p,str);
            }
        }
    }

    private void readGroups(){
        Config  config = legendaryGuild.getFileManager().getConfig();

        //默认组
        List<String> requirements = config.getValue("settings.create.default.requirements",Arrays.asList("vault;100000"));
        this.defaultSection = Optional.of(new CreateGuildSection("default",null,-1,requirements));

        ConfigurationSection section = config.getSection("settings.create.groups").orElse(null);
        int a = 0;
        if (section != null){
            for (String id:section.getKeys(false)){
                String perm=section.getString(id+".permission",null);
                int weight=section.getInt(id+".weight",0);
                if (section.getStringList(id+".requirements") == null){
                    continue;
                }
                List<String> requirement=section.getStringList(id+".requirements");
                addGroup(new CreateGuildSection(id,perm,weight,requirement));
                a++;
            }
        }
        legendaryGuild.info("加载 "+a+ " 个公会创建组. & Load "+a+" guild creation groups.", Level.INFO);
    }



    public LinkedList<CreateGuildSection> getGroups() {
        return sort_weight;
    }

     private LinkedList<CreateGuildSection> sortSections(){
        LinkedList<CreateGuildSection> sort = new LinkedList<>();
        for (Map.Entry<String,CreateGuildSection> entry:cache_byId.entrySet()){
            sort.add(entry.getValue());
        }
        Collections.sort(sort, new Comparator<CreateGuildSection>() {
            @Override
            public int compare(CreateGuildSection o1, CreateGuildSection o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });
        return sort;
    }
    public void addGroup(CreateGuildSection section){
        cache_byId.put(section.getId(),section);
    }

    public CreateGuildSection getGroup(String id){
        return cache_byId.get(id);
    }
    public Optional<CreateGuildSection> getDefault(){
        return defaultSection;
    }
}
