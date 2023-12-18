package com.legendaryrealms.LegendaryGuild.Data.Others;

import java.util.List;

public class ActivityReward {
    private String id;
    private String display;
    private double points;
    private List<String> run;

    public ActivityReward(String id, String display, double points, List<String> run) {
        this.id = id;
        this.display = display;
        this.points = points;
        this.run = run;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public double getPoints() {
        return points;
    }

    public List<String> getRun() {
        return run;
    }
}
