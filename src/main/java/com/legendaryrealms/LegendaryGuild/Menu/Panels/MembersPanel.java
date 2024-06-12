package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildActivityData;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.MembersLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class MembersPanel extends MenuDraw {
    private int page;
    private Sort sort;
    private final MembersLoader load= (MembersLoader) getLoader();
    public MembersPanel(Player p, int page, Sort sort) {
        super(p, "Members");
        this.page = page;
        this.sort = sort;
        this.inv = Bukkit.createInventory(this,load.getSize(),load.getTitle().replace("%sort%",getPlaceHolder(sort.getPlaceholder())));
        this.slotToMember = new HashMap<>();

        DrawEssentailSpecial(inv,menuItem -> {
            if (menuItem.getFuction().equals("sort")){
                ItemStack i = menuItem.getI();

                ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                        .addSinglePlaceHolder("placeholder_sort",getPlaceHolder(sort.getPlaceholder()));

                menuItem.setI(replaceHolderUtils.startReplace(i,false,null));
            }
        });
        loadPage();
    }

    @Override
    public void onDrag(InventoryDragEvent e) {

    }
    private HashMap<Integer,String> slotToMember;
    public void loadPage(){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                List<String> members = getPage(page);
                if (!members.isEmpty()){
                    Guild guild = UserAPI.getGuild(p.getName()).get();
                    GuildActivityData activityData = LegendaryGuild.getInstance().getGuildActivityDataManager().getData(guild.getGuild());
                    for (String target : members){
                        User targetUser = LegendaryGuild.getInstance().getUsersManager().getUser(target);
                        Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(targetUser.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
                        //5.1.7.3版本之前创建公会时会长的入会日期不会更新，所以在此进行一次更新
                        if (targetUser.getDate() == null || targetUser.getDate().isEmpty()) {
                            targetUser.setDate(guild.getDate());
                            targetUser.update(false);
                        }

                        ItemStack i =new ItemStack(load.getMm_icon(),1,(short) load.getMm_data());
                        ItemMeta id = i.getItemMeta();
                        id.setDisplayName(load.getMm_display().replace("%player%",target));
                        List<String> lore = load.getMm_lore();
                        lore = LegendaryGuild.getInstance().getHookManager().getPlaceholderAPIHook().replaceHolder(lore, Bukkit.getOfflinePlayer(target));
                        lore.replaceAll(l -> l.replace("%date%",targetUser.getDate())
                                .replace("%total_points%",""+targetUser.getTotal_points())
                                .replace("%position%",position.getDisplay())
                                .replace("%activity%",String.valueOf(activityData.getPlayerActivity(target)))
                                .replace("%total_activity%",String.valueOf(activityData.getPlayerTotalActivity(target))));
                        id.setLore(lore);
                        if (LegendaryGuild.getInstance().version_high){
                            id.setCustomModelData(load.getMm_model());
                        }

                        i.setItemMeta(id);
                        inv.setItem(getLayout().get(a), i);
                        slotToMember.put(getLayout().get(a), target);
                        a++;
                    }
                }
            }
        });
    }

    public List<String> getPage(int page){
        List<String> members = getBy(sort);
        List<String> returnList = new ArrayList<>();
        int start = 0 + (page-1)*getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        for (int get = start ; get<end ; get++){
            if (members.size() > get){
                returnList.add(members.get(get));
            }
        }
        return returnList;
    }

    public List<String> getBy(Sort sort){

        Guild guild = UserAPI.getGuild(p.getName()).get();
        LinkedList<String> members = guild.getMembers();
        List<String> list = members.stream().collect(Collectors.toList());
        if (sort.equals(Sort.DATE)){
            return list;
        }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                User u1 = LegendaryGuild.getInstance().getUsersManager().getUser(o1);
                User u2 = LegendaryGuild.getInstance().getUsersManager().getUser(o2);
                if ( u1 == null ) {
                    return  1;
                }
                if (u2 == null) {
                    return -1;
                }
                if (u1 != null && u2 != null) {
                    switch (sort) {
                        case POSITION: {
                            Position p1 = LegendaryGuild.getInstance().getPositionsManager().getPosition(u1.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
                            Position p2 = LegendaryGuild.getInstance().getPositionsManager().getPosition(u2.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
                            return (p2.getWeight() > p1.getWeight()) ? -1 : ((p2.getWeight() == p1.getMax()) ? 0 : 1);
                        }
                        case POINTS: {
                            return (u1.getTotal_points() > u2.getTotal_points()) ? -1 : ((u1.getTotal_points() == u2.getTotal_points()) ? 0 : 1);
                        }
                    }
                }
                return 0;
            }
        });
        return list;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())){
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null){
                switch (menuItem.getFuction()){
                    case "pre":{
                        if (page <= 1){
                            return;
                        }
                        MembersPanel membersPanel = new MembersPanel(p,page-1,sort);
                        membersPanel.loadPage();
                        membersPanel.open();
                        break;
                    }
                    case "next" : {
                        if (getPage(page+1).isEmpty()){
                            return;
                        }
                        MembersPanel membersPanel = new MembersPanel(p,page+1,sort);
                        membersPanel.loadPage();
                        membersPanel.open();
                        break;
                    }
                    case "sort" : {
                        int max = Sort.values().length;
                        int index = sort.ordinal();
                        int next = index+1 == max ? 0 : index+1;
                        Sort nextSort = Sort.values()[next];
                        MembersPanel membersPanel = new MembersPanel(p,1,nextSort);
                        membersPanel.loadPage();
                        membersPanel.open();
                        break;
                    }
                }
            }
            else {
                String member = slotToMember.get(e.getRawSlot());
                if (member != null){
                    if (e.isShiftClick() && e.isRightClick()) {
                        if (member.equals(p.getName())){
                            return;
                        }
                        User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
                        Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(user.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());

                        if (position.isKick()) {
                            if (UserAPI.kick(user.getGuild(),p,member)){
                                MembersPanel membersPanel = new MembersPanel(p,1,sort);
                                membersPanel.loadPage();
                                membersPanel.open();
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public enum Sort{
        POSITION("sort_position"),
        POINTS("sort_points"),
        DATE("sort_date");

        private String placeholder;
        Sort(String placeholder){
            this.placeholder = placeholder;
        }

        public String getPlaceholder() {
            return placeholder;
        }
    }
}
