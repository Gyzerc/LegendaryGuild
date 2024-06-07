package com.legendaryrealms.LegendaryGuild.Utils.BungeeCord;

public class NetWorkMessage {

    private NetWorkType netWorkType;
    private String target;

    public NetWorkMessage(NetWorkType netWorkType, String target) {
        this.netWorkType = netWorkType;
        this.target = target;
    }

    public NetWorkType getNetWorkType() {
        return netWorkType;
    }

    public String getTarget() {
        return target;
    }

    public String toString(){
        StringBuilder builder=new StringBuilder();
        return builder.append(netWorkType).append(";").append(target).toString();
    }

    public enum NetWorkType {
        UPDATE_USER,
        UPDATE_GUILD,
        REMOVE_GUILD,
        SCAN_PLAYER_IS_OPEN_STORE,
        UPDATE_REDPACKEY,
        UPDATE_GUILD_SHOP,
        UPDATE_GUILD_ACTIVITY_DATA,
        REFRESH_ACTIVITY,
        UPDATE_TEAMSHOPDATA,
        UPDATE_STORE;

    }

}
