package com.legendaryrealms.LegendaryGuild.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplaceHolderUtils {

    private List<SingleHolder> singles;
    private List<ListHolder> lists;


    public ReplaceHolderUtils(){
        this.singles = new ArrayList<>();
        this.lists = new ArrayList<>();
    }

    public ReplaceHolderUtils addSinglePlaceHolder(String target,String placeText){
        this.singles.add(new SingleHolder(target,placeText));
        return this;
    }

    public ReplaceHolderUtils addListPlaceHolder(String target,List<String> placeText){
        this.lists.add(new ListHolder(target,placeText));
        return this;
    }

    public ItemStack startReplace(ItemStack item, boolean usePlaceHolderAPI, String holderPlayerName){
        ItemStack i = item.clone();
        ItemMeta id = i.getItemMeta();
        if (id.hasDisplayName()){
            id.setDisplayName(replaceSingleText(id.getDisplayName()));
        }
        List<String> oldLore = id.hasLore() ? id.getLore() : new ArrayList<>();
        if (lists.isEmpty()){
            oldLore.replaceAll( l -> {
                return replaceSingleText(l);
            });
            if (usePlaceHolderAPI){
                oldLore = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(holderPlayerName), oldLore);
            }
            id.setLore(oldLore);
            i.setItemMeta(id);
            return i;
        }
        List<String> newLore = new ArrayList();
        for (String l : oldLore){
            boolean hasChange = false;
            for (ListHolder listHolder : lists){
                String target = "%"+listHolder.getTarget() + "%";
                if (l.contains(target)){
                    for (String a : listHolder.getValue()){
                        newLore.add(l.replace(target,a));
                    }
                    hasChange = true;
                    break;
                }
            }
            if (!hasChange){
                newLore.add(replaceSingleText(l));
            }
        }
        if (usePlaceHolderAPI){
            newLore = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(holderPlayerName), newLore);
        }
        id.setLore(newLore);
        i.setItemMeta(id);
        return i;
    }

    private String replaceSingleText(String str){
        String returnStr = str;
        for (SingleHolder singleHolder : singles){
             returnStr = returnStr.replace("%"+singleHolder.getTarget()+"%",singleHolder.getValue());
        }
        return returnStr;
    }

    public class ListHolder {
        private String target;
        private List<String> value;

        public ListHolder(String target, List<String> value) {
            this.target = target;
            this.value = value;
        }

        public String getTarget() {
            return target;
        }

        public List<String> getValue() {
            return value;
        }
    }
    public class SingleHolder {

        private String target;
        private String value;

        public SingleHolder(String target, String value) {
            this.target = target;
            this.value = value;
        }

        public String getTarget() {
            return target;
        }

        public String getValue() {
            return value;
        }
    }
}
