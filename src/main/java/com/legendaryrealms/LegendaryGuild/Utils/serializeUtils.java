package com.legendaryrealms.LegendaryGuild.Utils;

import com.google.gson.Gson;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.logging.Level;

public class serializeUtils {

    private static final LegendaryGuild legendaryguild=LegendaryGuild.getInstance();

    public static StringStore StringToStringStore(String str){
        StringStore stringStore = new StringStore();
        if (str != null && !str.isEmpty()){
            for (String sub : str.split("⁝")){
                String[] data = sub.split(":");
                stringStore.setValue(data[0],data[1],null);
            }
        }
        return stringStore;
    }

    public static StringStore StringToActivityData(String str){
        StringStore stringStore = new StringStore();
        if (str != null && !str.isEmpty()){
            for (String sub : str.split("⁝")){
                String[] data = sub.split(":");
                stringStore.setValue(data[0],StrToList(data[1]),null);
            }
        }
        return stringStore;
    }

    public static HashMap<String,Integer> StrToMap_string_int(String str){
        HashMap<String,Integer> map = new HashMap<>();
        String[] args = str.split(";");
        for (String arg:args){
            if (arg.isEmpty()){
                continue;
            }
            try {
                String[] sub=arg.split(",");
                String id = sub[0];
                int amount = Integer.parseInt(sub[1]);
                map.put(id,amount);
            }
            catch (ClassCastException e){
                legendaryguild.info("转化数据类型出错！ -> "+arg, Level.SEVERE,e);
            }
            catch (NullPointerException e){
                legendaryguild.info("获取内容出错！ -> "+arg,Level.SEVERE,e);
            }
        }
        return map;
    }


    public static List<String> StrToList(String str){
        if (str == null || str.isEmpty()){
            return new ArrayList<>();
        }
        String[] args = str.split(",");
        return Arrays.asList(args);
    }

    public static String ListToStr(List<String> list){
        StringBuilder builder=new StringBuilder();
        for (String str:list){
            builder.append(str).append(",");
        }
        return builder.toString();
    }

    public static LinkedList<String> StrToLinkList(String str){
        if (str == null || str.isEmpty()){
            return new LinkedList<>();
        }
        String[] args = str.split(",");
        LinkedList list = new LinkedList();
        for (String arg:args){
            list.add(arg);
        }
        return list;
    }

    public static String LinkListToStr(LinkedList<String> list){
        StringBuilder builder=new StringBuilder();
        for (String str:list){
            builder.append(str).append(",");
        }
        return builder.toString();
    }

    public static String ApplicationsToStr(LinkedList<Guild.Application> application){
        StringBuilder builder=new StringBuilder();
        application.forEach(application1 -> {
            builder.append(application1.getPlayer()).append(",").append(application1.getDate()).append(";");
        });
        return builder.toString();
    }

    public static LinkedList<Guild.Application> StrToApplications(String str){
        str = str.replace(" ","");
        if (str == null || str.isEmpty()){
            return new LinkedList<>();
        }
        LinkedList<Guild.Application> list = new LinkedList<>();
        for (String sub : str.split(";")){
            String[] appStr=sub.split(",");
            list.add(new Guild.Application(appStr[0],appStr[1]));
        }
        return list;

    }

    public static String LocationToStr(Guild.GuildHomeLocation loc){
        if (loc == null){
            return null;
        }
        StringBuilder builder=new StringBuilder();
        builder.append(loc.getWorld()).append(";").append(loc.getX()).append(";").append(loc.getY()).append(";").append(loc.getZ());
        return builder.toString();
    }
    public static Guild.GuildHomeLocation StrToLocation(String server,String str){
        if (str == null || str.isEmpty()){
            return null;
        }
        String[] args = str.split(";");
        World world= Bukkit.getWorld(args[0]);
        if (world == null){
            return null;
        }
        return new Guild.GuildHomeLocation(args[0],server,Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
    }




    public static String sts(String data) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(data);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sfs(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            return (String) bukkitObjectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
