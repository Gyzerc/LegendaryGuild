package com.legendaryrealms.LegendaryGuild.Data.Database;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild_Redpacket;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.GuildShopData;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Shop.Item.ShopType;
import com.legendaryrealms.LegendaryGuild.Data.User.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class DataProvider {

    abstract void initDataBase() throws RuntimeException;
    public abstract void setConnection() throws SQLException;
    public abstract void closeDataBase();
    public abstract void createTable(DatabaseTable table);
    public abstract boolean isExist(DatabaseTable table);
    public abstract Optional<User> getUser(String player);
    public abstract void saveUser(User user);
    public abstract List<String> getGuilds();
    public abstract Optional<Guild> getGuild(String guild);
    public abstract void saveGuild(Guild guild);
    public abstract GuildStore getStore(String guild);
    public abstract void saveStore(GuildStore store);
    public abstract Guild_Redpacket getRedPacket(String guild);
    public abstract void saveRedPacket(Guild_Redpacket redpacket);
    public abstract GuildShopData getGuildShopData();
    public abstract void saveGuildShopData(GuildShopData data);
    public abstract void deleteGuildShopData(String type);
    public enum DatabaseType {
        MYSQL,SQLite
    }

    public enum DatabaseTable{
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
                .build("type"));

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

        public Builder(String tableName) {
            this.tableName = tableName;
            stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+tableName+" (");
        }

        public Builder addTextKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" TEXT DEFAULT NULL");
            return this;
        }

        public Builder addUUIDKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" UUID DEFAULT NULL");
            return this;
        }

        public Builder addBlobKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" BLOB DEFAULT NULL");
            return this;
        }

        public Builder addIntegerKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` INTEGER NOT NULL");
            return this;
        }

        public Builder addDoubleKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` DOUBLE NOT NULL");
            return this;
        }
        public Builder addLongKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` LONG NOT NULL");
            return this;
        }
        public Builder addVarcharKey(String keyName,int length){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` varchar("+length+") NOT NULL");
            return this;
        }
        public Builder addBooleanKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` BOOLEAN NOT NULL");
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
    }
}
