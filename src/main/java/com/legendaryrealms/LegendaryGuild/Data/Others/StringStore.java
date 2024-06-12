package com.legendaryrealms.LegendaryGuild.Data.Others;

import com.legendaryrealms.LegendaryGuild.Utils.serializeUtils;

import java.util.*;

public class StringStore<T>{
    private HashMap<String,T> map;
    public StringStore() {
        this.map = new HashMap<>();
    }
    public void setValue(String a,T v,T def){
        T set = (v !=null ? v : def);
        map.put(a,set);
    }
    public T getValue(String a,T def){
        return map.getOrDefault(a, def);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        try {
            for (Map.Entry<String, T> entry : map.entrySet()){
                T value = entry.getValue();
                String add = value.toString();
                if (value instanceof List){
                    add = serializeUtils.ListToStr((List<String>) value);
                }
                builder.append(entry.getKey()).append(":").append(add).append("⁝");
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
