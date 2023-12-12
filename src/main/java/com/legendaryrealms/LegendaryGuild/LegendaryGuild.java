package com.legendaryrealms.LegendaryGuild;

import com.google.common.collect.Iterables;
import com.legendaryrealms.LegendaryGuild.Manager.Guild.*;
import com.legendaryrealms.LegendaryGuild.Manager.Others.BuffsManager;
import com.legendaryrealms.LegendaryGuild.Manager.Others.TributesItemsManager;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWork;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkHandle;
import com.legendaryrealms.LegendaryGuild.Command.Commands;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Database.DataProvider;
import com.legendaryrealms.LegendaryGuild.Data.Database.MysqlStore;
import com.legendaryrealms.LegendaryGuild.Data.Database.SqliteStore;
import com.legendaryrealms.LegendaryGuild.Listener.MenuEvent;
import com.legendaryrealms.LegendaryGuild.Listener.PlayerJoin;
import com.legendaryrealms.LegendaryGuild.Listener.PlayerQuit;
import com.legendaryrealms.LegendaryGuild.Manager.*;
import com.legendaryrealms.LegendaryGuild.Manager.Others.RequirementsManager;
import com.legendaryrealms.LegendaryGuild.Manager.Others.WaterPotsManager;
import com.legendaryrealms.LegendaryGuild.Manager.User.PositionsManager;
import com.legendaryrealms.LegendaryGuild.Manager.User.UsersManager;
import com.legendaryrealms.LegendaryGuild.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LegendaryGuild extends JavaPlugin implements PluginMessageListener {
    public boolean version_high;
    private static List<String> players;
    private static LegendaryGuild legendaryGuild;
    public Lang.LangType lang = Lang.LangType.Chinese;
    public String SERVER = "Server";

    @Override
    public void onEnable() {

        long time = System.currentTimeMillis();

        players = new ArrayList<>();
        legendaryGuild = this;
        //获取是否高版本
        version_high = BukkitVersionHigh();

        //初始化基础模块
        initEssentails();

        //创建数据库
        loadDatabase();

        //加载数据
        reloadData();

        //注册指令
        Bukkit.getPluginCommand("legendaryguild").setExecutor(new Commands());
        Bukkit.getPluginCommand("legendaryguild").setTabCompleter(new Commands());
        Commands.register();

        //注册监听器
        registerListener();

        //注册跨服同步
        netWork = new NetWorkHandle(this);

        //加载所有公会到缓存中
        sync(new Runnable() {
            @Override
            public void run() {
                guildsManager.loadGuilds();
                if (fileManager.getStores().isEnable()){
                    storesManager.loadStores();
                }
            }
        });

        //获取全服在线玩家
        sync(new Runnable() {
            @Override
            public void run() {
                if (netWork.isEnable()) {
                    Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                    if (p != null) {
                        netWork.PlayerList(p);
                    }
                }
            }
        },20,20);

        //检测公会商店周期
        sync(new Runnable() {
            @Override
            public void run() {
                guildShopDataManager.checkDate();
            }
        },20,200);

        Bukkit.getConsoleSender().sendMessage(fileManager.getLang().plugin+msgUtils.msg("&a插件启动成功！ 耗时&e"+(System.currentTimeMillis()-time) +"ms"));
    }

    @Override
    public void onDisable() {

        //断开数据库连接
        dataProvider.closeDataBase();
        netWork.disable();

     }
    public static void setPlayers(List<String> player){
        players = player;
     }
    private void initEssentails(){

        msgUtils = new MsgUtils(this);
        positionsManager = new PositionsManager(this);
        fileManager = new FileManager();
        requirementsManager = new RequirementsManager(this);
        hookManager = new HookManager(this);
        createGroupsManager = new CreateGroupsManager(this);
        waterPotsManager = new WaterPotsManager(this);
        guildIconsManager = new GuildIconsManager(this);
        tributesItemsManager = new TributesItemsManager(this);
        guildShopItemsManager = new GuildShopItemsManager(this);
        if (fileManager.getBuffFile().getEnable()){
            buffsManager = new BuffsManager(this);
        }


        menuLoadersManager = new MenuLoadersManager(this);
    }

    public void reloadData(){

        usersManager = new UsersManager(this);
        guildsManager = new GuildsManager(this);
        redPacketsManager = new GuildRedPacketsManager(this);
        guildShopDataManager = new GuildShopDataManager(this);
        if (fileManager.getStores().isEnable()) {
            storesManager = new GuildStoresManager(this);
        }

    }

    private void registerListener(){
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(),this);
        Bukkit.getPluginManager().registerEvents(new MenuEvent(),this);
    }

    //注册BC通讯通道
    public void registerBungeecordChannel(){
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }
    public void unregisterBungeecordChannel(){
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        netWork.recive(s,player,bytes);
    }



    public void setServerName(String name){
        this.SERVER = name;
    }
    public static LegendaryGuild getInstance(){return legendaryGuild;}
    public HookManager getHookManager() {
        return hookManager;
    }
    public DataProvider getDataBase() {
        return dataProvider;
    }

    public GuildsManager getGuildsManager() {
        return guildsManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
    public UsersManager getUsersManager() {
        return usersManager;
    }
    public RequirementsManager getRequirementsManager(){return requirementsManager;}

    public CreateGroupsManager getCreateGroupsManager() {
        return createGroupsManager;
    }

    public static List<String> getPlayers() {
        return players;
    }

    public NetWork getNetWork() {
        return netWork;
    }

    public MsgUtils getMsgUtils() {
        return msgUtils;
    }

    public PositionsManager getPositionsManager() {
        return positionsManager;
    }

    public GuildStoresManager getStoresManager() {
        return storesManager;
    }

    public MenuLoadersManager getMenuLoadersManager() {
        return menuLoadersManager;
    }

    public GuildRedPacketsManager getRedPacketsManager() {
        return redPacketsManager;
    }

    public WaterPotsManager getWaterPotsManager() {return waterPotsManager;}

    public GuildIconsManager getGuildIconsManager() {
        return guildIconsManager;
    }

    public TributesItemsManager getTributesItemsManager() {
        return tributesItemsManager;
    }

    public GuildShopDataManager getGuildShopDataManager() {
        return guildShopDataManager;
    }

    public GuildShopItemsManager getGuildShopItemsManager() {
        return guildShopItemsManager;
    }

    public BuffsManager getBuffsManager() {
        return buffsManager;
    }

    private MsgUtils msgUtils;
    private GuildsManager guildsManager;
    private NetWork netWork;
    private DataProvider dataProvider;
    private FileManager fileManager;
    private RequirementsManager requirementsManager;
    private HookManager hookManager;
    private CreateGroupsManager createGroupsManager;
    private UsersManager usersManager;
    private PositionsManager positionsManager;
    private MenuLoadersManager menuLoadersManager;
    private GuildStoresManager storesManager;
    private GuildRedPacketsManager redPacketsManager;
    private WaterPotsManager waterPotsManager;
    private GuildIconsManager guildIconsManager;
    private TributesItemsManager tributesItemsManager;
    private GuildShopDataManager guildShopDataManager;
    private GuildShopItemsManager guildShopItemsManager;
    private BuffsManager buffsManager;

    private void loadDatabase(){
        DataProvider.DatabaseType type = fileManager.getConfig().store;
        switch (type){
            case MYSQL:
                dataProvider = new MysqlStore(this);
                break;
            case SQLite:
                dataProvider = new SqliteStore(this);
                break;
        }
    }
    public boolean BukkitVersionHigh() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        int version = Integer.parseInt(name.substring(name.indexOf("_") + 1, name.indexOf("R") - 1));
        return (version >= 13);
    }
    public String color(String msg){return msgUtils.msg(msg);}
    public List<String> color(List<String> msg){return msgUtils.msg(msg);}
    public void info(String msg, Level level,Throwable throwable){
        getLogger().log(level,msg,throwable);
    }
    public void info(String msg, Level level){
        getLogger().log(level,msg);
    }
    public List<String> getOnlinePlayersName(){
        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
    }
    public boolean checkIsNumber(String arg) {
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(arg);
        return isNum.matches();
    }
    public void sync(Runnable consumer){
        Bukkit.getScheduler().runTaskAsynchronously(legendaryGuild,consumer);
    }
    public void sync(Runnable runnable,int delay){
        Bukkit.getScheduler().runTaskLaterAsynchronously(legendaryGuild,runnable,delay);
    }
    public void sync(Runnable runnable,int delay,int timer){
        Bukkit.getScheduler().runTaskTimerAsynchronously(legendaryGuild,runnable,delay,timer);
    }
    public String getDate(){
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(System.currentTimeMillis());
        return date;
    }
}
