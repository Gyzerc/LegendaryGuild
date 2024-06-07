package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.IntStore;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.ApplicationsLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ApplicationsPanel extends MenuDraw {
    private int page;
    private final ApplicationsLoader loader = (ApplicationsLoader) getLoader();
    public ApplicationsPanel(Player p, int page) {
        super(p, "Applications");
        this.page = page;
        this.inv = Bukkit.createInventory(this,loader.getSize(),loader.getTitle());
        DrawEssentail(inv);
        loadPage();
    }

    private HashMap<Integer,String> slotToApp;
    public void loadPage(){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                if (getPage(page).isEmpty()){
                    return;
                }
                int a =0;
                User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
                Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
                for (Guild.Application application:getPage(page, guild.getApplications())){
                    String player = application.getPlayer();
                    String date = application.getDate();

                    ItemStack i = new ItemStack(loader.getApp_icon(),1,(short)loader.getApp_data());
                    ItemMeta id = i.getItemMeta();
                    id.setDisplayName(loader.getApp_display().replace("%player%",player));
                    List<String> lore = loader.getApp_lore();
                    lore = LegendaryGuild.getInstance().getHookManager().getPlaceholderAPIHook().replaceHolder(lore, Bukkit.getOfflinePlayer(player));
                    lore.replaceAll(l->l
                            .replace("%apply_date%",date));
                    id.setLore(lore);

                    if (LegendaryGuild.getInstance().version_high){
                        id.setCustomModelData(loader.getApp_model());
                    }
                    i.setItemMeta(id);

                    inv.setItem(getLayout().get(a),i );
                    specialSlot.setValue(getLayout().get(a),player,null);
                    a++;
                }
            }
        });
    }

    public LinkedList<Guild.Application> getPage(int page){
        User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
        Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
        LinkedList<Guild.Application> applications = guild.getApplications();

        LinkedList<Guild.Application> returnList = new LinkedList<>();

        int start = 0 + (page-1)*getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        for (int get = start;start<end;start++){
            if (applications.size() > get){
                returnList.add(applications.get(get));
            }
        }
        return applications;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())){
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null){
                switch (menuItem.getFuction()){
                    case "pre": {
                        if (page <= 1){
                            return;
                        }
                        ApplicationsPanel applicationsPanel = new ApplicationsPanel(p,(page-1));
                        applicationsPanel.loadPage();
                        applicationsPanel.open();
                        break;
                    }
                    case "next": {
                        if (getPage(page+1).isEmpty()){
                            return;
                        }
                        ApplicationsPanel applicationsPanel = new ApplicationsPanel(p,(page+1));
                        applicationsPanel.loadPage();
                        applicationsPanel.open();
                        break;
                    }
                }
            }
            else {
                String target = (String) specialSlot.getValue(e.getRawSlot(),null);
                if (target != null){
                    User clickUser = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
                    Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(clickUser.getGuild());
                    Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(clickUser.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
                    if (position.isAccept()){

                        if (e.isRightClick()){

                            guild.removeApplication(target);
                            guild.update();

                            p.sendMessage(lang.plugin+lang.application_deny.replace("%value%",target));
                            LegendaryGuild.getInstance().getMsgUtils().sendMessage(target,lang.plugin+lang.application_deny_target.replace("%value%",guild.getDisplay()));

                            ApplicationsPanel applicationsPanel = new ApplicationsPanel(p,page);
                            applicationsPanel.loadPage();
                            applicationsPanel.open();
                            return;

                        }

                        User targetUser = LegendaryGuild.getInstance().getUsersManager().getUser(target);
                        if (targetUser.hasGuild()){
                            guild.removeApplication(target);
                            guild.update();

                            ApplicationsPanel applicationsPanel = new ApplicationsPanel(p,page);
                            applicationsPanel.loadPage();
                            applicationsPanel.open();
                            return;
                        }
                        if (guild.getMembers().size() >= guild.getMaxMembers()){
                            p.sendMessage(lang.plugin+lang.member_max);
                            return;
                        }
                        guild.removeApplication(target);
                        guild.update();

                        UserAPI.JoinGuild(targetUser,guild);

                        ApplicationsPanel applicationsPanel = new ApplicationsPanel(p,page);
                        applicationsPanel.loadPage();
                        applicationsPanel.open();
                        return;
                    }
                    p.sendMessage(lang.plugin+lang.nopass_position);
                    return;
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
}
