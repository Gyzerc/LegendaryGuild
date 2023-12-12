package com.legendaryrealms.LegendaryGuild.Data.Others;

import java.util.*;

public class StringStore<T>{
    private HashMap<String,T> map;
    public StringStore() {
        this.map = new HashMap<>();
    }
    public void setValue(String a,T v,T def){
        T set = v !=null ? v : def;
        map.put(a,set);
    }
    public T getValue(String a,T def){
        return map.getOrDefault(a, def);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        try {
            for (Map.Entry<String, T> entry : map.entrySet()){
                builder.append(entry.getKey()).append(":").append(String.valueOf(entry.getValue())).append("⁝");
            }
            return builder.toString();
        } catch (ClassCastException e){
            return "";
        }
    }

    public List<String> Values(){
        return new ArrayList<>(map.keySet());
    }
}
