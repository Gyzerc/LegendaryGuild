package com.legendaryrealms.LegendaryGuild.Data.Database;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild_Redpacket;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Data.User.WaterDataStore;
import com.legendaryrealms.LegendaryGuild.Utils.serializeUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class MysqlStore extends DataProvider{
    private HikariDataSource connectPool;
    private LegendaryGuild legendaryGuild;
    public MysqlStore(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        initDataBase();
    }
    @Override
    void initDataBase() throws RuntimeException {
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
    }

    @Override
    public void setConnection() {
        setConnectPool();
    }

    private void closeCon(Connection connection){
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeDataBase() {
        if (connectPool != null && !connectPool.isClosed()){
            connectPool.close();
            legendaryGuild.info("成功关闭MySQL数据库连接.",Level.INFO);
        }
    }

    @Override
    public void createTable(DatabaseTable table) {
        if (isExist(table)){
            return;
        }
        if (executeUpdate(table.getBuilder().toString())){
            legendaryGuild.info("成功创建表 "+table.getName(),Level.INFO);
        }
    }

    @Override
    public boolean isExist(DatabaseTable table) {
        if (connectPool == null){
            return false;
        }
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `"+table.getBuilder().getMainKey()+"` FROM `"+table.getName()+"` LIMIT 1;");
            statement.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeCon(connection);
        }
    }
    @Override
    public Optional<String> getSystemData(String key) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.SYSTEM_PLACEHODER.getName() + " WHERE `name` = '" + key + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(rs.getString("value"));
            }
        } catch (SQLException e){
            legendaryGuild.info("获取系统变量数据时出错！ ",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return Optional.empty();
    }

    @Override
    public void saveSystemData(String key,String value) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.SYSTEM_PLACEHODER.getName()+" (`name`,`value`) VALUES(?,?)");
            ps.setString(1,key);
            ps.setString(2,value);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存系统变量数据时出错！ ",Level.SEVERE,ex);
        } finally {
            closeCon(connection);
        }
    }
    @Override
    public Optional<User> getUser(String player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
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
        } finally {
            closeCon(connection);
        }
        return Optional.empty();
    }

    @Override
    public void saveUser(User user){
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
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
        } catch (SQLException ex) {
            legendaryGuild.info("保存玩家数据时出错！ ",Level.SEVERE,ex);
        }
        closeCon(connection);
    }

    @Override
    public List<String> getGuilds() {
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> guilds = new ArrayList<>();
        try {
            connection = connectPool.getConnection();
            statement = connection.prepareStatement("SELECT `guild` FROM "+DatabaseTable.GUILD_DATA.getName()+";");
            ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String guild = resultSet.getString("guild");
                    guilds.add(guild);
                }
        }
        catch (SQLException e) {
            legendaryGuild.info("获取所有公会失败！",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return guilds;
    }

    @Override
    public Optional<Guild> getGuild(String guild) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
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

                Guild data = new Guild(guild,owner,icon,date,money,exp,treeexp,level,treelevel,intro,notice,friends,buff,unlock_icons,members,applications,location);

                return Optional.of(data);
            }
        }catch (SQLException ex) {
            legendaryGuild.info("获取公会数据时出错！ ",Level.SEVERE,ex);
        }finally {
            closeCon(connection);
        }
        return Optional.empty();

    }


    @Override
    public void deleteGuild(String string) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM `"+DatabaseTable.GUILD_DATA.getName()+"` WHERE guild=?");
            preparedStatement.setString(1, string);
            preparedStatement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            legendaryGuild.info("删除公会数据时出错！ ",Level.SEVERE,e);
        }finally {
            closeCon(connection);
        }
    }

    @Override
    public void saveGuild(Guild guild) {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_DATA.getName()+" (guild,owner,icon,intro,notice,members,applications,unlock_icons,home_server,home_location,date,friends,buffs,money,exp,treeexp,level,treelevel) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            ps.executeUpdate();
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会数据时出错！ ",Level.SEVERE,ex);
        }
        closeCon(connection);
    }

    @Override
    public GuildStore getStore(String guild) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.STORE_DATA.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                String data = serializeUtils.sfs(rs.getString("data"));
                GuildStore store = GuildStore.toStore(guild,data);
                closeCon(connection);
                return store;
            }
        } catch (SQLException e){
            legendaryGuild.info("获取公会仓库数据时出错！ ",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return new GuildStore(guild,new HashMap<>());
    }

    @SuppressWarnings("unlock")
    @Override
    public void saveStore(GuildStore store) {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.STORE_DATA.getName()+" (guild,data) VALUES(?,?)");
            ps.setString(1, store.getGuild());
            ps.setString(2, serializeUtils.sts(store.toString()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会仓库数据时出错！ ",Level.SEVERE,ex);
        }
        closeCon(connection);
    }

    @Override
    public Guild_Redpacket getRedPacket(String guild) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("SELECT * FROM " + DatabaseTable.GUILD_REDPACKET.getName() + " WHERE guild = '" + guild + "' LIMIT 1;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Guild_Redpacket redpacket = Guild_Redpacket.toData(guild,rs.getString("data"));
                closeCon(connection);
                return redpacket;
            }
        } catch (SQLException e){
            legendaryGuild.info("获取公会红包数据时出错！ ",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return new Guild_Redpacket(guild,new HashMap<>());
    }

    @Override
    public void saveRedPacket(Guild_Redpacket redpacket) {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_REDPACKET.getName()+" (guild,data) VALUES(?,?)");
            ps.setString(1, redpacket.getGuild());
            ps.setString(2, redpacket.toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会红包数据时出错！ ",Level.SEVERE,ex);
        }
        closeCon(connection);
    }

    @Override
    public GuildShopData getGuildShopData() {
        Calendar calendar = Calendar.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();

            int day = Integer.parseInt(getSystemData("last_date").orElse("0"));
            int week = Integer.parseInt(getSystemData("last_week").orElse("0"));
            int month = Integer.parseInt(getSystemData("last_month").orElse("0"));


            String[] args = new String[4];
            ShopType[] types = new ShopType[]{ShopType.Once,ShopType.Day,ShopType.Week,ShopType.Month};
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
        } finally {
            closeCon(connection);
        }

        int date = calendar.get(Calendar.DATE);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        return new GuildShopData(new HashMap<>(),date,week,month);
    }

    @Override
    public void saveGuildShopData(GuildShopData data) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_SHOP.getName()+" (type,data) VALUES(?,?)");

            ShopType[] types = new ShopType[]{ShopType.Once,ShopType.Day,ShopType.Week,ShopType.Month};
            for (int in = 0; in < types.length ; in++) {
                ShopType type = types[in];
                ps.setString(1, type.name());
                ps.setString(2, data.toString(type));
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            legendaryGuild.info("保存公会商店数据时出错！ ",Level.SEVERE,ex);
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public void deleteGuildShopData(String type) {
            Connection connection=null;
            PreparedStatement preparedStatement=null;
            try {
                connection = connectPool.getConnection();
                preparedStatement = connection.prepareStatement("DELETE FROM `"+DatabaseTable.GUILD_SHOP.getName()+"` WHERE type=?");
                preparedStatement.setString(1, type);
                preparedStatement.execute();
            } catch (SQLException e) {
                legendaryGuild.info("删除会商店数据时出错！ ",Level.SEVERE,e);
            } finally {
                closeCon(connection);
            }
    }



    @Override
    public Optional<GuildActivityData> getGuildActivityData(String guild) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = connectPool.getConnection();
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
        } finally {
            closeCon(connection);
        }
        return Optional.empty();
    }

    @Override
    public void saveGuildActivityData(GuildActivityData data) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = connectPool.getConnection();
            ps = connection.prepareStatement("REPLACE INTO "+DatabaseTable.GUILD_ACTIVITY_DATA.getName()+" (guild,points,claimed) VALUES(?,?,?)");
            ps.setString(1, data.getGuild());
            ps.setDouble(2,data.getPoints());
            ps.setString(3,data.getClaimed().toString());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            legendaryGuild.info("保存公会活跃度数据时出错！ ",Level.SEVERE,ex);
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public void deleteGuildActivityData(){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection = connectPool.getConnection();
            preparedStatement = connection.prepareStatement("TRUNCATE TABLE `"+DatabaseTable.GUILD_ACTIVITY_DATA.getName()+"`");
            preparedStatement.execute();
        } catch (SQLException e) {
            legendaryGuild.info("删除会活跃度数据时出错！ ",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
    }

    private void setConnectPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        Optional<ConfigurationSection> sectionOptional = legendaryGuild.getFileManager().getConfig().getSection("HikariCP");
        if (sectionOptional.isPresent()) {
            ConfigurationSection section = sectionOptional.get();
            hikariConfig.setConnectionTimeout(section.getLong("connectionTimeout"));
            hikariConfig.setMinimumIdle(section.getInt("minimumIdle"));
            hikariConfig.setMaximumPoolSize(section.getInt("maximumPoolSize"));
            sectionOptional = legendaryGuild.getFileManager().getConfig().getSection("Mysql");
            if (sectionOptional.isPresent()) {
                section = sectionOptional.get();
                String url = "jdbc:mysql://" + section.getString("address") + ":" + section.getString("port") + "/" + section.getString("database") + "?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai";
                hikariConfig.setJdbcUrl(url);
                hikariConfig.setUsername(section.getString("user"));
                hikariConfig.setPassword(section.getString("password"));
                hikariConfig.setAutoCommit(true);
                connectPool = new HikariDataSource(hikariConfig);
                return;
            }
            legendaryGuild.info("config.yml中缺少了 Mysql 配置,请重新生成配置文件进行修改..", Level.SEVERE);
            return;
        }
        legendaryGuild.info("config.yml中缺少了 HikariCP 配置,请重新生成配置文件进行修改..", Level.SEVERE);
    }
    public boolean executeUpdate(String execute) {
        Connection connection=null;
        Statement stat = null;
            try {
                connection = connectPool.getConnection();
                stat = connection.createStatement();
                stat.executeUpdate(execute);
                return true;
            } catch (SQLException e) {
                legendaryGuild.info("执行命令失败！ -> " + execute, Level.SEVERE, e);
                return false;
            }
    }


}
