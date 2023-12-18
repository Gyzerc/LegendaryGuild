package com.legendaryrealms.LegendaryGuild.Data.Guild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Utils.serializeUtils;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildStore {
    private String guild;
    private HashMap<Integer,StoreData> data;
    public GuildStore(String guild, HashMap<Integer, StoreData> data) {
        this.guild = guild;
        this.data = data;
    }

    public void setContents(int id,ItemStack[] itemStacks){
        if (data.containsKey(id)) {
            StoreData storeData = data.get(id);
            storeData.setContents(itemStacks);
            data.put(id,storeData);
        }
    }
    public void setWhite(int id,List<String> white){
        if (data.containsKey(id)){
            StoreData storeData = data.get(id);
            storeData.setWhite(white);
            data.put(id,storeData);
        }
    }
    public void setUse(int id,String player){
        if (data.containsKey(id))
        {
            StoreData storeData = data.get(id);
            storeData.setUse(player);
            data.put(id,storeData);
        }
    }
    public void unlock(int id){
        if (data.containsKey(id)){
            return;
        }
        data.put(id,new StoreData(id,new ArrayList<>(),new ItemStack[]{},null));
    }
    public String getGuild() {
        return guild;
    }

    public HashMap<Integer, StoreData> getData() {
        return data;
    }

    public void setData(HashMap<Integer, StoreData> data) {
        this.data = data;
    }
    public StoreData getData(int id){
        return data.get(id);
    }
    public boolean hasUnlock(int id){
        return data.containsKey(id);
    }
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        data.forEach((integer, storeData) -> {
            String nbt = NBT.itemStackArrayToNBT(storeData.getContents()).toString();
            builder.append(storeData.getId()).append("⁝").append(storeData.getUse()).append("⁝").append(serializeUtils.ListToStr(storeData.getWhite())).append("⁝").append(nbt).append("⁞");
        });
        return builder.toString();
    }

    public static GuildStore toStore(String guild,String str){

        if (str == null){
            return new GuildStore(guild,new HashMap<>());
        }
        if (str.isEmpty() ){
            return new GuildStore(guild,new HashMap<>());
        }
        HashMap<Integer,StoreData> map = new HashMap<>();
        for (String data:str.split("⁞")){
           String[] args = data.split("⁝");
           String ID = args[0];
           String use = args[1];
           List<String> white = serializeUtils.StrToList(args[2]);
           ItemStack[] contents = NBT.itemStackArrayFromNBT(NBT.parseNBT(args[3]));
           map.put(Integer.parseInt(ID),new StoreData(Integer.parseInt(ID),white,contents,use));
        }
        return new GuildStore(guild,map);

    }

    public void updata(){
        LegendaryGuild.getInstance().getStoresManager().update(this);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(),null);
        if (p != null) {
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_STORE, guild))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
        }
    }

    public static class StoreData implements Serializable {
        private int id;
        private List<String> white;
        private ItemStack[] contents;
        private String use;

        public StoreData(int id, List<String> white, ItemStack[] contents, String use) {
            this.id = id;
            this.white = white;
            this.contents = contents;
            this.use = use;
        }

        public int getId() {
            return id;
        }

        public List<String> getWhite() {
            return white;
        }

        public ItemStack[] getContents() {
            return contents;
        }

        public String getUse() {
            return use;
        }

        public void setWhite(List<String> white) {
            this.white = white;
        }

        public void setContents(ItemStack[] contents) {
            this.contents = contents;
        }

        public void setUse(String use) {
            this.use = use;
        }


    }
}
