package com.legendaryrealms.LegendaryGuild.Data.Others;

import java.util.HashMap;

public class IntStore<T> {
    private HashMap<Integer,T> map;

    public IntStore() {
        this.map = new HashMap<>();
    }

    public void setValue(int a,T v,T def){
        T set = v !=null ? v : def;
        map.put(a,set);
    }

    public T getValue(int a,T def){
        return map.getOrDefault(a, def);
    }
}
