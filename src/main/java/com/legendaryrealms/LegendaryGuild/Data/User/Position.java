package com.legendaryrealms.LegendaryGuild.Data.User;

public class Position {
    private String id;
    private String display;
    private int weight;
    private int max;

    public Position(String id, String display, int weight, int max) {
        this.id = id;
        this.display = display;
        this.weight = weight;
        this.max = max;
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
