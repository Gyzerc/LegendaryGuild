package com.legendaryrealms.LegendaryGuild.Data.Guild;



import java.util.Comparator;
import java.util.List;

public class CreateGuildSection  {
    private String id;
    private String perm;
    private int weight;
    private List<String> requirements;
    
    public CreateGuildSection(String id, String perm,int weight, List<String> requirements) {
        this.id = id;
        this.perm = perm;
        this.weight = weight;
        this.requirements = requirements;
    }

    public String getPerm() {
        return perm;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public List<String> getRequirements() {
        return requirements;
    }



}
