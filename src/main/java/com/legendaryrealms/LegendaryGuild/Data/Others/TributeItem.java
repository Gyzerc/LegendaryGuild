package com.legendaryrealms.LegendaryGuild.Data.Others;

import org.bukkit.Material;

public class TributeItem {
    private String id;
    private Material material;
    private int data;
    private String display;
    private double points;
    private double exp;
    private boolean broad;
    private String message;

    public TributeItem(String id, Material material, int data, String display, double points, double exp, boolean broad, String message) {
        this.id = id;
        this.material = material;
        this.data = data;
        this.display = display;
        this.points = points;
        this.exp = exp;
        this.broad = broad;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public int getData() {
        return data;
    }

    public String getDisplay() {
        return display;
    }

    public double getPoints() {
        return points;
    }

    public double getExp() {
        return exp;
    }

    public boolean isBroad() {
        return broad;
    }

    public String getMessage() {
        return message;
    }
}
