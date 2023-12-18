package com.legendaryrealms.LegendaryGuild.Data.User;

import java.util.List;

public class Position {
    private String id;
    private String display;
    private int weight;
    private int max;
    private List<String> attrs;

    public Position(String id, String display, int weight, int max,List<String> attrs) {
        this.id = id;
        this.display = display;
        this.weight = weight;
        this.max = max;
        this.attrs = attrs;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public int getWeight() {
        return weight;
    }

    public int getMax() {
        return max;
    }
}
