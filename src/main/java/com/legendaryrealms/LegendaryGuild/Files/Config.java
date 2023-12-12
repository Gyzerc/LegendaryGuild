package com.legendaryrealms.LegendaryGuild.Files;

import com.legendaryrealms.LegendaryGuild.Data.Database.DataProvider;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Config extends FileProvider{
    public Config(LegendaryGuild legendaryGuild) {
        super(legendaryGuild, "./plugins/LegendaryGuild","", "config.yml");


    }
    public DataProvider.DatabaseType store;
    public boolean CROSS_SERVER;
    public int max_length;


    public HashMap<Integer, Double> EXP;
    public int MAXLEVEL;
    public HashMap<Integer, Double> TREEEXP;
    public HashMap<Integer, List<String>> TREE_REQUIREMENTS;
    public int TREE_BAR_LENGTH;
    public String TREE_BAR_COMPLETED;
    public String TREE_BAR_EMPTRY;
    public int MAX_TREE_LEVEL;

    public HashMap<Integer, Integer> MEMBERS;

    public double MONEY_TO_POINTS;

    public int COOLDOWN;

    public HashMap<Integer, List<String>> WISH;
    public double MIN_REDPACKET_TOTAL;
    public int MIN_REDPACKET_AMOUNT;
    @Override
    public void readDefault() {

        EXP = new HashMap<>();
        TREEEXP = new HashMap<>();
        TREE_REQUIREMENTS = new HashMap<>();
        MEMBERS = new HashMap<>();
        WISH = new HashMap<>();


        try {
            store = DataProvider.DatabaseType.valueOf(getValue("Store","SQLite"));
        } catch (IllegalArgumentException e){
            legendaryGuild.info("数据储存方式填写出错！目前默认为SQLite.", Level.SEVERE);
            store = DataProvider.DatabaseType.SQLite;
        }
        try {
            legendaryGuild.lang = Lang.LangType.valueOf(getValue("lang","Chinese"));
        } catch (IllegalArgumentException e){
            legendaryGuild.info("语言文件填写出错！目前默认为Chinese.", Level.SEVERE);
            legendaryGuild.lang = Lang.LangType.Chinese;
        }
        CROSS_SERVER = getValue("settings.Cross_Server.enable",false);
        legendaryGuild.setServerName(getValue("settings.Cross_Server.server_name","Server"));

        max_length = getValue("settings.create.max-length",6);

        MONEY_TO_POINTS = getValue("settings.guild.moneyToPoints",0.1);

        COOLDOWN = getValue("settings.create.cooldown",240);

        //公会每级经验
        MAXLEVEL = getValue("settings.guild.level.max",5);
        List<Double> doubles = getValue("settings.guild.level.require", Arrays.asList(1000.0,5000.0,10000.0,50000.0,100000.0));
        for (int a = 0 ; a<MAXLEVEL ; a++){
            if (doubles.size() > a) {
                EXP.put(a, doubles.get(a));
            }
            else {
                EXP.put(a,9999999.9);
            }
        }
        //公会每级最大人数
        List<Integer> integers = getValue("settings.guild.level.maxmembers", Arrays.asList(5,10,15,20,25));
        for (int a = 0 ; a<MAXLEVEL ; a++){
            if (integers.size() > a) {
                MEMBERS.put(a, integers.get(a));
            }
            else {
                MEMBERS.put(a,100);
            }
        }

        //公会神树每级经验
        MAX_TREE_LEVEL = getValue("settings.guild.tree.level.max",5);
        doubles = getValue("settings.guild.tree.level.requireExp", Arrays.asList(1000.0,5000.0,10000.0,50000.0,100000.0));
        for (int a = 0 ; a<MAX_TREE_LEVEL ; a++){
            if (doubles.size() > a) {
                TREEEXP.put(a, doubles.get(a));
            }
            else {
                TREEEXP.put(a,9999999.9);
            }
        }

        //公会经验进度
        TREE_BAR_LENGTH = getValue("settings.guild.tree.level.bar.length",10);
        TREE_BAR_COMPLETED = legendaryGuild.color(getValue("settings.guild.tree.level.bar.completed","&a■"));
        TREE_BAR_EMPTRY = legendaryGuild.color(getValue("settings.guild.tree.level.bar.empty","&c■"));

        //公会神树升级花费
        ConfigurationSection section = getSection("settings.guild.tree.level.requirements").orElse(null);
        if (section == null){
            legendaryGuild.info("公会神树升级花费配置缺失！插件关闭.",Level.SEVERE);
            Bukkit.getPluginManager().disablePlugin(legendaryGuild);
            return;
        }
        for (String levelStr : section.getKeys(false)){
            TREE_REQUIREMENTS.put(Integer.parseInt(levelStr),getValue("settings.guild.tree.level.requirements."+levelStr,new ArrayList<>()));
        }

        //公会神树许愿奖励
        section = getSection("settings.guild.tree.wish").orElse(null);
        if (section == null){
            legendaryGuild.info("公会神树许愿配置缺失！插件关闭.",Level.SEVERE);
            Bukkit.getPluginManager().disablePlugin(legendaryGuild);
            return;
        }
        for (String levelStr : section.getKeys(false)){
            WISH.put(Integer.parseInt(levelStr),getValue("settings.guild.tree.wish."+levelStr+".run",new ArrayList<>()));
        }

        MIN_REDPACKET_AMOUNT = getValue("settings.redpacket.min_amount",2);
        MIN_REDPACKET_TOTAL = getValue("settings.min_total",100.0);

        saveYml();
    }
}
