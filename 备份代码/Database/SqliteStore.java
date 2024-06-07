package com.legendaryrealms.LegendaryGuild.Data.Database;

import com.legendaryrealms.LegendaryGuild.Data.Guild.*;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.Utils.serializeUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class SqliteStore extends DataProvider{
    final String FILE_NAME = "LegendaryGuildData.db";
    private LegendaryGuild legendaryGuild;
    private File folder;
    private Connection connection;
    public SqliteStore(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        this.folder = new File(legendaryGuild.getDataFolder(),FILE_NAME);
        initDataBase();
    }
    @Override
    void initDataBase() {

        //连接数据库
        setConnection();
        //创建表
        createTable(DatabaseTable.SYSTEM_PLACEHODER);
        createTable(DatabaseTable.GUILD_DATA);
        createTable(DatabaseTable.USER_DATA);
        if (legendaryGuild.getFileManager().getStores().isEnable()) {
            createTable(DatabaseTable.STORE_DATA);
        }
        createTable(DatabaseTable.GUILD_REDPACKET);
        createTable(DatabaseTable.GUILD_SHOP);
        createTable(DatabaseTable.GUILD_ACTIVITY_DATA);
        createTable(DatabaseTable.GUILD_TEAMSHOP);

    }

    @Override
    public void setConnection() {
        if (!folder.exists())
            try {
                folder.createNewFile();
                legendaryGuild.info("create the sqlite database & 成功创建SQLite数据库", Level.INFO);
            } catch (IOException e) {
                legendaryGuild.info("An exception occurred creating sqlite database & 创建SQLite数据库时出现问题", Level.SEVERE,e);
            }
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + folder);
            this.connection = connection;
            legendaryGuild.info("成功连接SQLite数据库",Level.INFO);
        }
        catch (SQLException ex) {
            legendaryGuild.info("An exception occurred initializing the sqlite database & 安装SQLite数据库时出现问题", Level.SEVERE,ex);
        } catch (ClassNotFoundException ex) {
            legendaryGuild.info("Failed to found the SQLite driver & 缺少SQLite依赖库", Level.SEVERE,ex);
        }
    }


    @Override
    public void closeDataBase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                legendaryGuild.info("成功断开SQLite数据库连接 & Successfully disconnected SQLite database connection",Level.INFO);
            }
        } catch (SQLException e) {
            legendaryGuild.info("Failed to close the connection & 关闭SQLite数据库出现错误",Level.SEVERE, e);
        }
    }

    @Override
    public void createTable(DatabaseTable table) {

        if (isExist(table)){
            checkTable(table,connection);
            return;
        }
        if (executeUpdate(table.getBuilder().toString())){
            legendaryGuild.info("成功创建表 "+table.getName(),Level.INFO);
        }
    }

    @Override
    public boolean isExist(DatabaseTable table) {
        if (!this.folder.exists()){
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `"+table.getBuilder().getMainKey()+"` FROM `"+table.getName()+"` LIMIT 1;");
            statement.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    @Override
    public Optional<String> getSystemData(String key) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.SYSTEM_PLACEHODER.getName() + " WHERE name = '" + key + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(rs.getString("value"));
            }
        } catch (SQLException ex){
            legendaryGuild.info("获取系统变量数据时出错！ ",Level.SEVERE,ex);
        }
        return Optional.empty();
    }

    @Override
    public void saveSystemData(String key,String value) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.SYSTEM_PLACEHODER.getName()+" (name,value) VALUES(?,?)");
            ps.setString(1,key);
            ps.setString(2,value);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存系统变量数据时出错！ ",Level.SEVERE,ex);
        }
    }


    @Override
    public Optional<GuildTeamShopData> getGuildTeamShopData(String guild) {
        Optional<ResultSet> resultSetOptional = getDataStringResult(connection, DatabaseTable.GUILD_TEAMSHOP.getBuilder(), guild);
        if (resultSetOptional.isPresent()) {
            ResultSet resultSet = resultSetOptional.get();
            try {
                String id = resultSet.getString("id");
                double current = resultSet.getDouble("current");
                HashMap<String, Double> bargains = getMap(resultSet.getString("bargains"));
                HashMap<String, Integer> buy = getMap(resultSet.getString("buy"));
                return Optional.of(new GuildTeamShopData(guild, id, current, bargains, buy));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setGuildTeamShopData(GuildTeamShopData data) {
        setData(connection, DatabaseTable.GUILD_TEAMSHOP.getBuilder(), data.getGuild(),
                data.getGuild(),
                data.getTodayShopId(),
                data.getCurrentPrice(),
                getMapString(data.getBargains()),
                getMapString(data.getBuy())
        );
    }


    @Override
    public void clearGuildTeamShopData(String guild) {
        PreparedStatement ps = null;
        try {
            if (guild != null) {
                ps = connection.prepareStatement("DELETE FROM  "+DatabaseTable.GUILD_TEAMSHOP.getName());
            } else {
                ps = connection.prepareStatement("DELETE FROM `"+DatabaseTable.GUILD_TEAMSHOP.getName()+"` WHERE guild=?");
                ps.setString(1,guild);
            }
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("刷新公会团购数据出错！ ",Level.SEVERE,ex);
        }
    }

    @Override
    public List<String> getUsers() {
        PreparedStatement statement = null;
        List<String> guilds = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM "+DatabaseTable.USER_DATA.getName()+";");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String guild = resultSet.getString("player");
                guilds.add(guild);
            }
        }
        catch (SQLException e) {
            legendaryGuild.info("获取所有用户失败！",Level.SEVERE,e);
        }
        return guilds;
    }
    @Override
    public Optional<User> getUser(String player) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM "+DatabaseTable.USER_DATA.getName()+" WHERE player = '" + player + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {

                String guild = rs.getString("guild");
                String position = rs.getString("position");

                String date = rs.getString("date") != null ? rs.getString("date") : "...";

                String pvpStr = rs.getString("pvp");
                User.PvpType pvp = User.PvpType.getType(pvpStr);

                boolean wish = rs.getBoolean("wish");
                boolean teleport_guild_home = rs.getBoolean("teleport_guild_home");
                double points = rs.getDouble("points");
                double total_points = rs.getDouble("total_points");
                int cooldown = rs.getInt("cooldown");


                //解析 浇水水壶数据
                String water_day = rs.getString("water_today");
                String water_total = rs.getString("water_total");
                HashMap<String,Integer> day = serializeUtils.StrToMap_string_int(water_day);
                HashMap<String,Integer> total = serializeUtils.StrToMap_string_int(water_total);

                HashMap<String, WaterDataStore.WaterData> water = new HashMap<>();
                for (Map.Entry<String,Integer> entry:total.entrySet()){
                    String id = entry.getKey();
                    int value_day=day.containsKey(id) ? day.get(id) : 0;
                    int value_total=total.containsKey(id) ? total.get(id) : 0;
                    water.put(id,new WaterDataStore.WaterData(id,value_day,value_total));
                }
                WaterDataStore store=new WaterDataStore(water);

                return Optional.of(new User(player, guild, position, date, store, cooldown, wish, teleport_guild_home, points, total_points, pvp));
            }
        } catch (SQLException ex) {
            legendaryGuild.info("获取玩家数据时出错！ ",Level.SEVERE,ex);
        }
        return Optional.empty();
    }

    @Override
    public void saveUser(User user) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.USER_DATA.getName()+" (player,guild,position,date,water_today,water_total,points,total_points,cooldown,wish,teleport_guild_home,pvp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, user.getPlayer());
            ps.setString(2, user.getGuild());
            ps.setString(3, user.getPosition());
            ps.setString(4, user.getDate());

            WaterDataStore store=user.getWaterDataStore();
            String day = store.toString_Day();
            String total = store.toString_Total();
            ps.setString(5,day);
            ps.setString(6,total);

            ps.setDouble(7,user.getPoints());
            ps.setDouble(8,user.getTotal_points());

            ps.setInt(9,user.getCooldown());

            ps.setBoolean(10,user.isWish());
            ps.setBoolean(11,user.isTeleport_guild_home());
            ps.setString(12,user.getPvp().toString());

            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存玩家数据时出错！ ",Level.SEVERE,ex);
        }
    }
    @Override
    public List<String> getGuilds() {
        PreparedStatement statement = null;
        List<String> guilds = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM "+DatabaseTable.GUILD_DATA.getName()+";");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String guild = resultSet.getString("guild");
                guilds.add(guild);
            }

        }
        catch (SQLException e) {
            legendaryGuild.info("获取所有公会失败！",Level.SEVERE,e);
        }
        return guilds;
    }
    @Override
    public Optional<Guild> getGuild(String guild) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.GUILD_DATA.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                String owner = rs.getString("owner");
                String icon = rs.getString("icon");

                String date = rs.getString("date") != null ? rs.getString("date") : "...";

                List<String> intro = serializeUtils.StrToList(rs.getString("intro"));
                List<String> notice = serializeUtils.StrToList(rs.getString("notice"));
                LinkedList<String> members = serializeUtils.StrToLinkList(rs.getString("members"));
                LinkedList<Guild.Application> applications = serializeUtils.StrToApplications(rs.getString("applications"));
                List<String> unlock_icons = serializeUtils.StrToList(rs.getString("unlock_icons"));
                List<String> friends = serializeUtils.StrToList(rs.getString("friends"));
                StringStore buff = serializeUtils.StringToStringStore(rs.getString("buffs"));

                String home_server = rs.getString("home_server");
                Guild.GuildHomeLocation location = serializeUtils.StrToLocation(home_server,rs.getString("home_location"));

                double money = rs.getDouble("money");
                double exp = rs.getDouble("exp");
                double treeexp = rs.getDouble("treeexp");

                int level = rs.getInt("level");
                int treelevel = rs.getInt("treelevel");
                int extra_members = rs.getInt("extra_members");

                return Optional.of(new Guild(guild,owner,icon,date,money,exp,treeexp,level,treelevel,intro,notice,friends,buff,unlock_icons,members,applications,location,extra_members));
            }
        }catch (SQLException ex) {
            legendaryGuild.info("获取公会数据时出错！ ",Level.SEVERE,ex);
        }
        return null;

    }

    @Override
    public void deleteGuild(String string) {
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `"+DatabaseTable.GUILD_DATA.getName()+"` WHERE guild=?");
            preparedStatement.setString(1, string);
            preparedStatement.execute();
        } catch (SQLException e) {
            legendaryGuild.info("删除公会数据时出错！ ",Level.SEVERE,e);
        }
    }

    @Override
    public void saveGuild(Guild guild) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_DATA.getName()+" (guild,owner,icon,intro,notice,members,applications,unlock_icons,home_server,home_location,date,friends,buffs,money,exp,treeexp,level,treelevel,extra_members) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, guild.getGuild());
            ps.setString(2, guild.getOwner());
            ps.setString(3, guild.getIcon());
            ps.setString(11, guild.getDate());

            ps.setString(4, serializeUtils.ListToStr(guild.getIntro()));
            ps.setString(5, serializeUtils.ListToStr(guild.getNotice()));
            ps.setString(8, serializeUtils.ListToStr(guild.getUnlock_icons()));
            ps.setString(12, serializeUtils.ListToStr(guild.getFriends()));
            ps.setString(13, guild.getBuffs().toString());


            ps.setString(6, serializeUtils.LinkListToStr(guild.getMembers()));
            ps.setString(7, serializeUtils.ApplicationsToStr(guild.getApplications()));


            Guild.GuildHomeLocation location = guild.getHome();
            String server = location!=null ? location.getServer() : legendaryGuild.SERVER;
            String loc = location != null ? serializeUtils.LocationToStr(location) : null;
            ps.setString(9,server);
            ps.setString(10,loc);

            ps.setDouble(14,guild.getMoney());
            ps.setDouble(15,guild.getExp());
            ps.setDouble(16,guild.getTreeexp());
            ps.setInt(17,guild.getLevel());
            ps.setInt(18,guild.getTreelevel());
            ps.setInt(19,guild.getExtra_members());

            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会数据时出错！ ",Level.SEVERE,ex);
        }
    }
    @Override
    public Guild_Redpacket getRedPacket(String guild) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.GUILD_REDPACKET.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Guild_Redpacket redpacket = Guild_Redpacket.toData(guild,rs.getString("data"));
                return redpacket;
            }
        } catch (SQLException e){
            legendaryGuild.info("获取公会红包数据时出错！ ",Level.SEVERE,e);
        }
        return new Guild_Redpacket(guild,new HashMap<>());
    }

    @Override
    public void saveRedPacket(Guild_Redpacket redpacket) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO " + DatabaseTable.GUILD_REDPACKET.getName() + " (guild,data) VALUES(?,?)");
            ps.setString(1, redpacket.getGuild());
            ps.setString(2, redpacket.toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会红包数据时出错！ ", Level.SEVERE, ex);
        }
    }

    @Override
    public GuildShopData getGuildShopData() {
        Calendar calendar = Calendar.getInstance();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            int day = Integer.parseInt(getSystemData("last_date").orElse("0"));
            int week = Integer.parseInt(getSystemData("last_week").orElse("0"));
            int month = Integer.parseInt(getSystemData("last_month").orElse("0"));

            String[] args = new String[4];
            ShopType[] types = new ShopType[]{ShopType.ONCE,ShopType.DAY,ShopType.WEEK,ShopType.MONTH};
            for (int in = 0; in < types.length ; in++) {
                ShopType type = types[in];
                ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.GUILD_SHOP.getName() + " WHERE type = '"+type.name()+"' LIMIT 1;");
                rs = ps.executeQuery();
                while (rs.next()) {
                    args[in] = rs.getString("data");
                }
            }
            return GuildShopData.getData(day,week,month,args);
        } catch (SQLException e){
            legendaryGuild.info("获取公会商店数据时出错！ ",Level.SEVERE,e);
        }
        int date = calendar.get(Calendar.DATE);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        return new GuildShopData(new HashMap<>(),date,week,month);
    }

    @Override
    public void saveGuildShopData(GuildShopData data) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_SHOP.getName()+" (type,data) VALUES(?,?)");

            ShopType[] types = new ShopType[]{ShopType.ONCE,ShopType.DAY,ShopType.WEEK,ShopType.MONTH};
            for (int in = 0; in < types.length ; in++) {
                ShopType type = types[in];
                ps.setString(1, type.name());
                ps.setString(2, data.toString(type));
                ps.executeUpdate();
            }
            ps.setString(1, "date");
            ps.setString(2, data.getLast_date()+";"+ data.getLast_week()+";"+data.getLast_month());
            ps.executeUpdate();

            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会商店数据时出错！ ",Level.SEVERE,ex);
        }
    }
    @Override
    public void deleteGuildShopData(String type) {
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `"+DatabaseTable.GUILD_SHOP.getName()+"` WHERE type=?");
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            legendaryGuild.info("保存公会商店数据时出错！ ",Level.SEVERE,e);
        }
    }

    @Override
    public List<String> getGuildActivityDatas() {
        PreparedStatement statement = null;
        List<String> guilds = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM "+DatabaseTable.GUILD_ACTIVITY_DATA.getName()+";");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String guild = resultSet.getString("guild");
                guilds.add(guild);
            }
        }
        catch (SQLException e) {
            legendaryGuild.info("获取所有公会活跃度失败！",Level.SEVERE,e);
        }
        return guilds;
    }
    @Override
    public Optional<GuildActivityData> getGuildActivityData(String guild) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.GUILD_ACTIVITY_DATA.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                double points = rs.getDouble("points");
                String claimedStr = rs.getString("claimed");
                StringStore claimed = serializeUtils.StringToActivityData(claimedStr);
                return Optional.of(new GuildActivityData(guild,points,claimed));
            }
        } catch (SQLException ex){
            legendaryGuild.info("获取公会活跃度数据时出错！ ",Level.SEVERE,ex);
        }
        return Optional.empty();
    }

    @Override
    public void saveGuildActivityData(GuildActivityData data) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_ACTIVITY_DATA.getName()+" (guild,points,claimed) VALUES(?,?,?)");
            ps.setString(1, data.getGuild());
            ps.setDouble(2,data.getPoints());
            ps.setString(3,data.getClaimed().toString());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会活跃度数据时出错！ ",Level.SEVERE,ex);
        }
    }


    @Override
    public void closeCon(Connection connection) {

    }


    public boolean executeUpdate(String execute) {
        if (connection != null) {
            try {
                Statement stat = null;
                stat = connection.createStatement();
                stat.executeUpdate(execute);
                return true;
            } catch (SQLException e) {
                legendaryGuild.info("执行命令失败！ -> " + execute, Level.SEVERE, e);
                return false;
            }
        }
        return false;
    }



    @Override
    public GuildStore getStore(String guild) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.STORE_DATA.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                String data = rs.getString("data");
                return GuildStore.toStore(guild,data);
            }
        } catch (SQLException e){
            legendaryGuild.info("获取公会仓库数据时出错！ ",Level.SEVERE,e);
        }
        return new GuildStore(guild,new HashMap<>());
    }

    @Override
    public void saveStore(GuildStore store) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.STORE_DATA.getName()+" (guild,data) VALUES(?,?)");
            ps.setString(1, store.getGuild());
            ps.setString(2, store.toString());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会仓库数据时出错！ ",Level.SEVERE,ex);
        }
    }
}
