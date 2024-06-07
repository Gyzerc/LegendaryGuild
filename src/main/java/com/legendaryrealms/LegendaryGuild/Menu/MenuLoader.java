package com.legendaryrealms.LegendaryGuild.Menu;

import com.legendaryrealms.LegendaryGuild.Files.FileProvider;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

public abstract class MenuLoader extends FileProvider {

    private String title;
    private Optional<Sound> sound;
    private int size;
    public HashMap<Integer,MenuItem> item;
    public HashMap<String,String> placeholder;
    public List<Integer> layout;

    public String findPlaceholderIgnore(String id ){
        for (Map.Entry<String,String> entry : placeholder.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(id)){
                return entry.getValue();
            }
        }
        return null;
    }
    public MenuLoader(LegendaryGuild legendaryGuild, String path, String internalPath, String fileName) {
        super(legendaryGuild, path, internalPath, fileName);

        item = new HashMap<>();
        placeholder = new HashMap<>();
        layout = new ArrayList<>();

        readEssentails();
        readSpecials();
    }

    protected void readSpecials() {
    }

    private void readEssentails(){

        if (yml.getString("layout")!=null){
            layout = deserializeSlot(yml.getString("layout", " "));
        }
        this.title = legendaryGuild.color(getValue("title"," "));
        this.size = getValue("size",54);
        this.sound = getSound(yml.getString("sound"));

        getSection("customItem").ifPresent(sec -> {
            for (String key:sec.getKeys(false)){
                String display = legendaryGuild.color(sec.getString(key+".display",""));
                Material material = getMaterial(sec.getString(key+".material","STONE"));
                int amount = sec.getInt(key+".amount",1);
                int data = sec.getInt(key+".data",0);
                int model = sec.getInt(key+".model",0);
                List<String> lore = legendaryGuild.color(sec.getStringList(key+".lore"));
                String fuction = sec.getString(key+".fuction.type","none");
                String value = sec.getString(key+".fuction.value","");

                ItemStack i = new ItemStack(material,amount,(short) data);
                ItemMeta id = i.getItemMeta();
                id.setDisplayName(display);
                id.setLore(lore);
                if (legendaryGuild.version_high){
                    id.setCustomModelData(model);
                }
                i.setItemMeta(id);
                List<Integer> slots=deserializeSlot(sec.getString(key+".slot",""));
                MenuItem menuItem = new MenuItem(key,slots,i,fuction,value);
                for (int slot:slots){
                    item.put(slot,menuItem);
                }
            }
        });
        getSection("placeholder").ifPresent(sec -> {
            for (String id:sec.getKeys(false)){
                placeholder.put(id,legendaryGuild.color(sec.getString(id)));
            }
        });

    }

    public Optional<Sound> getSound() {
        return sound;
    }

    public HashMap<Integer, MenuItem> getItem() {
        return item;
    }

    public List<Integer> getLayout() {
        return layout;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuItem getMenuItem(int slot){
        return item.get(slot);
    }

    public String getPlaceHolder(String key){
        return placeholder.get(key) != null ? placeholder.get(key) : "";
    }

    private Optional<Sound> getSound(String sound){
        if (sound == null){
            return Optional.empty();
        }
        try {
            return Optional.of(Sound.valueOf(sound.toUpperCase()));
        } catch (Exception e){
            legendaryGuild.info("音效ID出错！"+file.getName()+ "-> "+sound, Level.SEVERE);
            return Optional.empty();
        }
    }

    public Material getMaterial(String arg){
        String str=arg.toUpperCase();
        Material material = Material.getMaterial(str);
        if (material == null){
            material = Material.STONE;
            legendaryGuild.info("ID配置出错！"+file.getName()+" 该版本不存在该物品ID: "+arg,Level.SEVERE);
        }
        return material;
    }

    public List<Integer> deserializeSlot(String str){
        if (str == null){
            return new ArrayList<>();
        }
        String deal=str.replace("[","").replace("]","");
        if (deal.isEmpty()){
            return new ArrayList<>();
        }
        String[] args=deal.split(",");
        List<Integer> slot=new ArrayList<>();
        for (String slots:args){
            if (slots.contains("-")){
                int before=Integer.parseInt(slots.split("-")[0].replace(" ",""));
                int after=Integer.parseInt(slots.split("-")[1].replace(" ",""));
                for (int start=before;start <=after;start++){
                    slot.add(start);
                }
            }
            else{
                slot.add(Integer.parseInt(slots.replace(" ","")));
            }
        }
        return slot;
    }
}
