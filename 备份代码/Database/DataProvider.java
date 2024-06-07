package com.legendaryrealms.LegendaryGuild.Data.Database;

import com.legendaryrealms.LegendaryGuild.Data.Guild.*;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public abstract class DataProvider {

    abstract void initDataBase() throws RuntimeException;
    public abstract void setConnection() throws SQLException;
    public abstract void closeDataBase();
    public abstract void createTable(DatabaseTable table);
    public abstract boolean isExist(DatabaseTable table);
    public abstract Optional<String> getSystemData(String key);
    public abstract void saveSystemData(String key,String value);
    public abstract List<String> getUsers();
    public abstract Optional<User> getUser(String player);
    public abstract void saveUser(User user);
    public abstract List<String> getGuilds();
    public abstract Optional<Guild> getGuild(String guild);
    public abstract void deleteGuild(String string);
    public abstract void saveGuild(Guild guild);
    public abstract GuildStore getStore(String guild);
    public abstract void saveStore(GuildStore store);
    public abstract Guild_Redpacket getRedPacket(String guild);
    public abstract void saveRedPacket(Guild_Redpacket redpacket);
    public abstract GuildShopData getGuildShopData();
    public abstract void saveGuildShopData(GuildShopData data);
    public abstract void deleteGuildShopData(String type);

    public abstract Optional<GuildActivityData> getGuildActivityData(String guild);
    public abstract void saveGuildActivityData(GuildActivityData data);
    public abstract List<String> getGuildActivityDatas();
    public abstract void closeCon(Connection connection);

    public abstract Optional<GuildTeamShopData> getGuildTeamShopData(String guild);
    public abstract void setGuildTeamShopData(GuildTeamShopData data);

    public abstract void clearGuildTeamShopData(String guildName);


    public void checkTable(DatabaseTable table,Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            Builder builder = table.getBuilder();
            for (Builder.Column key : builder.keys) {
                if (!metaData.getColumns(null,null,builder.getTableName(), key.getColumn()).next()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("ALTER TABLE " + builder.getTableName() + " ADD COLUMN " + key.getColumn()+" " + key.getType());
                    LegendaryGuild.getInstance().info("检测到表 "+builder.getTableName() +" 缺失列 "+key.getColumn()+" 已自动补全..",Level.INFO);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public<T> HashMap<String,T> getMap(String str) {
        HashMap<String,T> map = new HashMap<>();
        if (str != null && !str.isEmpty()) {
            for (String args : str.split(";")) {
                String[] values = args.split("=");
                map.put(values[0], (T) values[1]);
            }
        }
        return map;
    }
    public<T> String getMapString(HashMap<String,T> map) {
        StringBuilder builder = new StringBuilder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String,T> entry : map.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
            }
        }
        return builder.toString();
    }

    protected Optional<ResultSet> getDataStringResult(Connection connection, DataProvider.Builder builder, String target) {
        if (connection != null) {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM " + builder.getTableName() + " WHERE `" + builder.getMainKey() + "` = '" + target + "' LIMIT 1;");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return Optional.of(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    protected Optional<ResultSet> getDataStrings(Connection connection, DataProvider.Builder builder) {
        if (connection != null) {
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM "+builder.getTableName()+";");
                rs = statement.executeQuery();
                return Optional.of(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
    protected void delData(Connection connection, DataProvider.Builder builder, String target) {
        if (connection != null) {
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement("DELETE FROM `"+builder.getTableName()+"` WHERE `"+builder.getMainKey()+"` = ?");
                statement.setString(1, target);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected <T> void setData(Connection connection, DataProvider.Builder builder, String target, T... ts) {
        if (connection != null) {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(builder.getInsertString(target));
                int a = 1;
                for (T t : ts) {
                    ps.setObject(a, t);
                    a++;
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public enum DatabaseType {
        MYSQL,SQLite
    }

    public enum DatabaseTable{
        GUILD_TEAMSHOP("guild_teamshop",new Builder("guild_teamshop")
                .addVarcharKey("guild",32)
                .addTextKey("id")
                .addDoubleKey("current")
                .addTextKey("bargains")
                .addTextKey("buy")
                .build("guild")),
        SYSTEM_PLACEHODER("system_placeholder",new Builder("system_placeholder")
                .addVarcharKey("name",32)
                .addTextKey("value")
                .build("name")),

        GUILD_DATA("guild_data",
                new Builder("guild_data")
                        .addVarcharKey("guild",32)
                        .addTextKey("owner")
                        .addTextKey("icon")
                        .addTextKey("intro")
                        .addTextKey("notice")
                        .addTextKey("members")
                        .addTextKey("applications")
                        .addTextKey("unlock_icons")
                        .addTextKey("home_server")
                        .addTextKey("home_location")
                        .addTextKey("date")
                        .addTextKey("friends")
                        .addTextKey("buffs")
                        .addDoubleKey("money")
                        .addDoubleKey("exp")
                        .addDoubleKey("treeexp")
                        .addIntegerKey("level")
                        .addIntegerKey("treelevel")
                        .addIntegerKey("extra_members")
                        .build("guild")
        ),
        USER_DATA("user_data",
                new Builder("user_data")
                        .addVarcharKey("player",32)
                        .addTextKey("guild")
                        .addTextKey("position")
                        .addTextKey("date")
                        .addTextKey("water_today")
                        .addTextKey("water_total")
                        .addDoubleKey("points")
                        .addDoubleKey("total_points")
                        .addIntegerKey("cooldown")
                        .addBooleanKey("wish")
                        .addBooleanKey("teleport_guild_home")
                        .addTextKey("pvp")
                        .build("player")
        ),
        STORE_DATA("store_data",new Builder("store_data")
                .addVarcharKey("guild",32)
                .addTextKey("data")
                .build("guild")),

        GUILD_REDPACKET("guild_redpacket",new Builder("guild_redpacket")
                .addVarcharKey("guild",32)
                .addTextKey("data")
                .build("guild")),

        GUILD_SHOP("guild_shop",new Builder("guild_shop")
                .addVarcharKey("type",32)
                .addTextKey("data")
                .build("type")),

        GUILD_ACTIVITY_DATA("guild_activity_data",new Builder("guild_activity_data")
                .addVarcharKey("guild",32)
                .addDoubleKey("points")
                .addTextKey("claimed")
                .build("guild"));


        private String name;
        private Builder builder;
        DatabaseTable(String name,Builder builder){
            this.name = name;
            this.builder = builder;
        }
        public String getName() {
            return name;
        }

        public Builder getBuilder() {
            return builder;
        }
    }


    public static class Builder {
        private String tableName;
        private String mainKey;
        private StringBuilder stringBuilder;
        private List<Column> keys;

        public Builder(String tableName) {
            this.keys = new ArrayList<>();
            this.tableName = tableName;
            stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+tableName+" (");
        }

        public Builder addTextKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" TEXT DEFAULT NULL");
            keys.add(new Column(keyName,"TEXT NOT NULL"));
            return this;
        }

        public Builder addUUIDKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" UUID DEFAULT NULL");
            keys.add(new Column(keyName,"UUID NOT NULL"));
            return this;
        }

        public Builder addBlobKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" BLOB DEFAULT NULL");
            keys.add(new Column(keyName,"BLOB NOT NULL"));
            return this;
        }

        public Builder addIntegerKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` INTEGER NOT NULL");
            keys.add(new Column(keyName,"INTEGER NOT NULL"));
            return this;
        }

        public Builder addDoubleKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` DOUBLE NOT NULL");
            keys.add(new Column(keyName,"DOUBLE NOT NULL"));
            return this;
        }
        public Builder addLongKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` LONG NOT NULL");
            keys.add(new Column(keyName,"LONG NOT NULL"));
            return this;
        }
        public Builder addVarcharKey(String keyName,int length){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` varchar("+length+") NOT NULL");
            keys.add(new Column(keyName,"varchar(" + length + ") NOT NULL"));
            return this;
        }
        public Builder addBooleanKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` BOOLEAN NOT NULL");
            keys.add(new Column(tableName,"BOOLEAN NOT NULL"));
            return this;
        }
        public Builder build(String mainKey){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            this.mainKey = mainKey;
            stringBuilder.append("PRIMARY KEY (`"+mainKey+"`));");
            return this;
        }

        public String getTableName() {
            return tableName;
        }

        public String getMainKey() {
            return mainKey;
        }

        @Override
        public String toString(){
            return stringBuilder.toString();
        }

        public String getInsertString(String target) { //`
            StringBuilder main = new StringBuilder("REPLACE INTO "+tableName+" ");
            StringBuilder keys = new StringBuilder("(");
            StringBuilder keys_unknow = new StringBuilder("(");
            for (int i =0 ; i < this.keys.size() ; i ++) {
                keys.append("`").append(this.keys.get(i)).append("`");
                keys_unknow.append("?");
                if (i == this.keys.size() - 1 ) {
                    keys.append(")");
                    keys_unknow.append(")");
                    break;
                } else {
                    keys.append(",");
                    keys_unknow.append(",");
                }
            }
            main.append(keys).append(" VALUES ").append(keys_unknow);
            return main.toString();
        }


        public class Column {
            private String column;
            private String type;

            public Column(String column, String type) {
                this.column = column;
                this.type = type;
            }

            public String getColumn() {
                return column;
            }

            public String getType() {
                return type;
            }
        }
    }

}
