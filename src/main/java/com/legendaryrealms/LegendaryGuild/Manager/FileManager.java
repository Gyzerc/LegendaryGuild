package com.legendaryrealms.LegendaryGuild.Manager;

import com.legendaryrealms.LegendaryGuild.Files.*;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class FileManager {

    private LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    public FileManager(){
        this.config = new Config(legendaryGuild);
        this.lang = new Lang(legendaryGuild);
        this.postionFile = new PostionFile(legendaryGuild);
        this.stores = new Stores(legendaryGuild);
        this.waterPotsFile = new WaterPotsFile(legendaryGuild);
        this.guildIconsFile = new GuildIconsFile(legendaryGuild);
        this.tributesFile = new TributesFile(legendaryGuild);
        this.guildShopFile = new GuildShopFile(legendaryGuild);
        this.buffFile = new BuffFile(legendaryGuild);
        this.activityRewardsFile = new ActivityRewardsFile(legendaryGuild);
        this.teamShopFile = new TeamShopFile(legendaryGuild);
    }

    public Config getConfig() {
        return config;
    }

    public Lang getLang() {
        return lang;
    }

    public PostionFile getPostionFile() {
        return postionFile;
    }

    public Stores getStores() {
        return stores;
    }

    public WaterPotsFile getWaterPotsFile() {
        return waterPotsFile;
    }

    public GuildIconsFile getGuildIconsFile() {
        return guildIconsFile;
    }

    public TributesFile getTributesFile() {
        return tributesFile;
    }

    public GuildShopFile getGuildShopFile() {
        return guildShopFile;
    }

    public BuffFile getBuffFile() {
        return buffFile;
    }

    public ActivityRewardsFile getActivityRewardsFile() {
        return activityRewardsFile;
    }

    public TeamShopFile getTeamShopFile() {
        return teamShopFile;
    }

    private Config config;
    private Lang lang;
    private PostionFile postionFile;
    private Stores stores;
    private WaterPotsFile waterPotsFile;
    private GuildIconsFile guildIconsFile;
    private TributesFile tributesFile;
    private GuildShopFile guildShopFile;
    private BuffFile buffFile;
    private ActivityRewardsFile activityRewardsFile;
    private TeamShopFile teamShopFile;

}
