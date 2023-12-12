package com.legendaryrealms.LegendaryGuild.Menu;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuItem {

    private String id;
    private List<Integer> layout;
    private ItemStack i;
    private String fuction;
    private String value;

    public MenuItem(String id, List<Integer> layout, ItemStack i, String fuction, String value) {
        this.id = id;
        this.layout = layout;
        this.i = i;
        this.fuction = fuction;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getLayout() {
        return layout;
    }

    public ItemStack getI() {
        return i;
    }

    public String getFuction() {
        return fuction;
    }

    public String getValue() {
        return value;
    }

    public MenuItem clone(){
        return new MenuItem(id,layout,i,fuction,value);
    }
    public void setI(ItemStack i) {
        this.i = i;
    }
}
