package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.Events.ActivityRewardClaimEvent;
import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.Others.ActivityReward;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.ActivityRewardsLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
import com.legendaryrealms.LegendaryGuild.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ActivityRewardsPanel extends MenuDraw {
    public ActivityRewardsPanel(Player p) {
        super(p, "ActivityRewards");
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());


        Guild guild = UserAPI.getGuild(p.getName()).orElse(null);
        GuildActivityData data = LegendaryGuild.getInstance().getGuildActivityDataManager().getData(guild.getGuild());

        StringStore store = data.getClaimed();
        DrawEssentailSpecial(inv,menuItem -> {
            if (menuItem.getFuction().equals("activity")){
                String activityId = menuItem.getValue();
                if (LegendaryGuild.getInstance().getActivityRewardsManager().getReward(activityId).isPresent()){


                    ActivityReward reward = LegendaryGuild.getInstance().getActivityRewardsManager().getReward(activityId).get();
                    List<String> claims = (List<String>) store.getValue(reward.getId(),new ArrayList<>());

                    String placeholder = getPlaceHolder("cant");
                    if (reward.getPoints() <= data.getPoints()){
                        if (claims.contains(p.getName())){
                            placeholder = getPlaceHolder("already");
                        }
                        else {
                            placeholder = getPlaceHolder("wait");
                        }
                    }

                    ItemStack i = menuItem.getI();
                    ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                            .addSinglePlaceHolder("activity",""+data.getPoints())
                            .addSinglePlaceHolder("placeholder",placeholder);
                    menuItem.setI(replaceHolderUtils.startReplace(i,true,p.getName()));
                }
            }
        });
    }



    @Override
    public void onClick(InventoryClickEvent e) {
        ActivityRewardsLoader loader = (ActivityRewardsLoader) getLoader();
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())) {
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()){
                    case "activity":{
                        String id = menuItem.getValue();
                        if (LegendaryGuild.getInstance().getActivityRewardsManager().getReward(id).isPresent()) {

                            Guild guild = UserAPI.getGuild(p.getName()).get();
                            GuildActivityData data = LegendaryGuild.getInstance().getGuildActivityDataManager().getData(guild.getGuild());
                            StringStore store = data.getClaimed();
                            ActivityReward reward = LegendaryGuild.getInstance().getActivityRewardsManager().getReward(id).get();


                            List<String> claims = new ArrayList<>( (List<String>) store.getValue(reward.getId(), new ArrayList<>()));

                            if (reward.getPoints() <= data.getPoints()){
                                if (claims.contains(p.getName())){
                                    p.sendMessage(lang.plugin+lang.activity_already_claimed);
                                    return;
                                }

                                claims.add(p.getName());
                                store.setValue(id,claims,new ArrayList<>());
                                data.setClaimed(store);
                                data.update();
                                Bukkit.getPluginManager().callEvent(new ActivityRewardClaimEvent(p,reward));

                                p.sendMessage(lang.plugin+lang.activity_claim.replace("%value%", reward.getDisplay()));
                                new RunUtils(reward.getRun(),p).start();


                                ActivityRewardsPanel activityRewardsPanel = new ActivityRewardsPanel(p);
                                activityRewardsPanel.open();
                                return;
                            }
                            p.sendMessage(lang.plugin+lang.activity_cant_claim.replace("%value%",""+reward.getPoints()));
                            return;
                        }
                            return;
                    }
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
}
