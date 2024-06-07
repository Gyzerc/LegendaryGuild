package com.legendaryrealms.LegendaryGuild.Manager.Others;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import com.legendaryrealms.LegendaryGuild.Requirement.Sub.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RequirementsManager {

    private LegendaryGuild legendaryGuild;
    private HashMap<String, Requirement> cache;
    public RequirementsManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        cache = new HashMap<>();

        //注册需求
        registerAll();
    }

    public void registerAll(){
        register(new VaultRequirement());
        register(new PlayerPointsRequirement());
        register(new ItemRequirement());
        register(new CustomItemRequirement());
        register(new GuildMoneyRequirement());
        register(new GuildLevelRequirement());
        register(new GuildTreeLevelRequirement());
        register(new GuildMembersRequirement());
        register(new GuildPointsRequirement());
        register(new ChanceRequirement());
        register(new GuildPositionRequirement());
        register(new PlaceholderRequirement());
        register(new ActivityRequirement());
        register(new TotalActivityRequirement());

    }

    public Optional<Requirement> serialize(String str){
        if (str == null){
            return Optional.empty();
        }
        if (str.isEmpty()){
            return Optional.empty();
        }
        if (str.contains(";")){
            String[] args=str.split(";");
            return cache.containsKey(args[0]) ? Optional.of(cache.get(args[0])) : Optional.empty();
        }
        return Optional.empty();
    }

    public void register(Requirement requirement){
        cache.put(requirement.getSymbol(),requirement);
    }

    public boolean check(Player p, List<String> requirements){
        if (requirements != null && !requirements.isEmpty()) {
            for (String str : requirements) {
                Requirement requirement = legendaryGuild.getRequirementsManager().serialize(str).orElse(null);
                if (requirement != null) {
                    if (!requirement.canPass(p, str)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void deal(Player p ,List<String> requirements){
        if (requirements != null && !requirements.isEmpty()) {
            for (String str : requirements) {
                Requirement requirement = legendaryGuild.getRequirementsManager().serialize(str).orElse(null);
                requirement.deal(p, str);
            }
        }
    }


}
