package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.StoresLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Utils.serializeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class StoresPanel extends MenuDraw {
    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private final StoresLoader loader = (StoresLoader) getLoader();
    private int page;
    public StoresPanel(Player p, int page) {
        super(p, "Stores");
        this.page = page;
        this.inv = Bukkit.createInventory(this,loader.getSize(),legendaryGuild.color(loader.getTitle()));
        this.slotToId = new HashMap<>();
        DrawEssentail(inv);
        load();
    }

    private HashMap<Integer,Integer> slotToId;
    public void load(){

        legendaryGuild.sync(new Runnable() {
            @Override
            public void run() {
                User user = legendaryGuild.getUsersManager().getUser(p.getName());
                Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());

                GuildStore store = legendaryGuild.getStoresManager().getStore(guild.getGuild());

                int max = legendaryGuild.getFileManager().getStores().getLEVEL_TO_MAX_STORES().getOrDefault(guild.getLevel(),5);
                int a = 0;
                int start = 0 + (page-1)*getLayout().size();
                int end = getLayout().size() + (page-1)*getLayout().size();
                for (int in = start;in < end ; in++){
                    if (store.hasUnlock(in)){
                        //获取仓库数据
                        GuildStore.StoreData data = store.getData(in);

                        ItemStack i = new ItemStack(loader.getStore_icon_unlock(),1,(short) loader.getStore_data_unlock());
                        ItemMeta id = i.getItemMeta();
                        id.setDisplayName(loader.getStore_display_unlock().replace("%id%",""+in));
                        List<String> lore = new ArrayList<>(loader.getStore_lore_unlock());
                        List<String> usersList = data.getWhite();

                        //获取可使用的成员名称
                        String users = getPlaceHolder("users_all");
                        if (!usersList.isEmpty() && usersList.size() > 0){
                            users = serializeUtils.ListToStr(usersList);
                        }
                        String finalUsers = users;

                        int full = getFull(data.getContents());
                        lore.replaceAll(l -> l
                                .replace("%full%",""+full)
                                .replace("%size%",""+legendaryGuild.getFileManager().getStores().getSIZE())
                                .replace("%users%", finalUsers));
                        id.setLore(lore);
                        if (legendaryGuild.version_high){
                            id.setCustomModelData(loader.getStore_model_unlock());
                        }
                        i.setItemMeta(id);
                        inv.setItem(getLayout().get(a), i);
                        slotToId.put(getLayout().get(a),in);
                    }
                    else {
                        ItemStack i = new ItemStack(loader.getStore_icon_locked(),1,(short) loader.getStore_data_locked());
                        ItemMeta id = i.getItemMeta();
                        id.setDisplayName(loader.getStore_display_locked().replace("%id%",""+in));
                        List<String> lore = new ArrayList<>(loader.getStore_lore_locked());
                        String canOrCant = in > max ? getPlaceHolder("cant") : getPlaceHolder("can");
                        lore.replaceAll(l -> l
                                .replace("%placeholder_LockOrCant%",canOrCant)
                                .replace("%money%",guild.getMoney()+""));
                        id.setLore(lore);
                        if (legendaryGuild.version_high){
                            id.setCustomModelData(loader.getStore_model_locked());
                        }
                        i.setItemMeta(id);
                        inv.setItem(getLayout().get(a), i);
                        slotToId.put(getLayout().get(a),in);
                    }
                    a++;
                }
            }
        });

    }


    public int getFull(ItemStack[] contents){
        int a  = 0;
        for (int get = 0 ;get < contents.length ; get++){
            ItemStack i = contents[get];
            if (i != null){
                if (i.getType().equals(Material.AIR)){
                    continue;
                }
                a++;
            }
        }
        return a;
    }
    public boolean hasPage(int page){
        User user = legendaryGuild.getUsersManager().getUser(p.getName());
        Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
        int max = legendaryGuild.getFileManager().getStores().getLEVEL_TO_MAX_STORES().getOrDefault(guild.getLevel(),5);

        int start = 0 + (page-1)*getLayout().size();
        if (start > max){
            return false;
        }
        return true;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())){
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem!= null){
                switch (menuItem.getFuction()){
                    case "pre": {
                        if (page <= 1){
                            return;
                        }
                        StoresPanel storesPanel = new StoresPanel(p,(page-1));
                        storesPanel.load();
                        storesPanel.open();
                        break;
                    }
                    case "next" : {
                        if (hasPage(page+1)){
                            StoresPanel storesPanel = new StoresPanel(p,(page+1));
                            storesPanel.load();
                            storesPanel.open();
                        }
                        break;
                    }
                }
            }
            else {
                if (slotToId.containsKey(e.getRawSlot())){
                    int id = slotToId.get(e.getRawSlot());
                    User user = legendaryGuild.getUsersManager().getUser(p.getName());
                    Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
                    GuildStore store = legendaryGuild.getDataBase().getStore(guild.getGuild());
                    if (store.hasUnlock(id)){
                        GuildStore.StoreData data = legendaryGuild.getStoresManager().getStore(guild.getGuild()).getData(id);
                        if (e.isShiftClick()){
                            if (!guild.getOwner().equals(p.getName())){
                                p.sendMessage(lang.plugin+lang.notowner);
                                return;
                            }
                            //添加信任成员
                            if (e.isLeftClick()){
                                p.closeInventory();
                                p.sendMessage(lang.plugin + lang.stores_add_white_title);
                                legendaryGuild.getChatControl().setModify(p.getUniqueId(),4,id+"");
                                return;
                            }
                            //删去信任成员
                            else if (e.isRightClick()){
                                p.closeInventory();
                                p.sendMessage(lang.plugin+ lang.stores_remove_white_title);
                                legendaryGuild.getChatControl().setModify(p.getUniqueId(),5,id+"");
                            }
                            return;
                        }

                        if (data.getUse() != null) {
                            scanhasPlayer(p,data.getUse(),id);
                            if (!data.getUse().equals("null")) {
                                p.sendMessage(lang.plugin + lang.stores_has_used);
                                return;
                            }
                        }
                        if (data.getWhite().size() > 0){
                            if (!guild.getOwner().equals(p.getName())){
                                if (!data.getWhite().contains(p.getName())){
                                    p.sendMessage(lang.plugin+lang.stores_cant_use);
                                    return;
                                }
                            }
                        }
                        store.setUse(id,p.getName());
                        StoreContainer container = new StoreContainer(guild .getGuild(),p,id,data.getContents());
                        //更新数据库
                        legendaryGuild.getStoresManager().update(store);
                    }
                    else {
                        int MAX = legendaryGuild.getFileManager().getStores().getLEVEL_TO_MAX_STORES().getOrDefault(guild.getLevel(),5);
                        if (id > MAX){
                            return;
                        }
                        if (!user.getPosition().equals(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
                            p.sendMessage(lang.plugin+lang.notowner);
                            return;
                        }
                        List<String> requirements = legendaryGuild.getFileManager().getStores().getREQUIREMENTS();
                        if (legendaryGuild.getRequirementsManager().check(p,requirements)){
                            legendaryGuild.getRequirementsManager().deal(p,requirements);
                            GuildAPI.unlockStore(p,guild,id);
                            StoresPanel storesPanel = new StoresPanel(p,page);
                            storesPanel.load();
                            storesPanel.open();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void scanhasPlayer(Player p,String use,int id) {
        Player tar = Bukkit.getPlayerExact(use);
        if (tar != null){
            if (tar.getOpenInventory().getTopInventory().getHolder() instanceof StoreContainer){
             return;
            }
            User user = legendaryGuild.getUsersManager().getUser(use);
            GuildStore store = legendaryGuild.getStoresManager().getStore(user.getGuild());
            store.setUse(id,"null");
            legendaryGuild.getStoresManager().update(store);
            //通知去其他服务器
            new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                    .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_STORE, user.getGuild()))
                    .setReciver("ALL")
                    .sendPluginMessage(p);
            return;
        }
        new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.SCAN_PLAYER_IS_OPEN_STORE,use+"/"+id))
                .setReciver("ALL")
                .sendPluginMessage(p);
    }


    @Override
    public void onDrag(InventoryDragEvent e) {

    }
    public class StoreContainer implements InventoryHolder {

        private Inventory inventory;
        private String guild;
        private Player p;
        private int id;

        public StoreContainer(String guild, Player p, int id,ItemStack[] contents) {
            this.guild = guild;
            this.p = p;
            this.id = id;
            this.inventory = Bukkit.createInventory(this,legendaryGuild.getFileManager().getStores().getSIZE(),legendaryGuild.getFileManager().getStores().getTITLE().replace("%id%",""+id));
            inventory.setContents(contents);
            p.openInventory(inventory);
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }

        public String getGuild() {
            return guild;
        }

        public Player getP() {
            return p;
        }

        public int getId() {
            return id;
        }

        public void onClick(InventoryClickEvent e) {
            if (e.isShiftClick()) {
                e.setCancelled(true);
                return;
            }
            if (e.getRawSlot() >= 0 && e.getRawSlot() < legendaryGuild.getFileManager().getStores().getSIZE()) {
                ItemStack i = e.getCursor();
                if (i != null && !i.getType().equals(Material.AIR)) {
                    if (!canPlace(i)) {
                        e.setCancelled(true);
                        p.sendMessage(lang.plugin + lang.stores_cant_input);
                        return;
                    }
                }
            }
        }

        public void onDrag(InventoryDragEvent e) {
            e.setCancelled(true);
        }
        private boolean canPlace(ItemStack i) {
            if (i != null) {
                List<Material> materials = legendaryGuild.getFileManager().getStores().getMaterials();
                if (!materials.isEmpty()) {
                    if (materials.contains(i.getType())) {
                        return false;
                    }
                }
                List<String> displays = legendaryGuild.getFileManager().getStores().getDisplay();
                if (i.hasItemMeta()) {
                    String name = i.getItemMeta().hasDisplayName() ? i.getItemMeta().getDisplayName() : "";
                    if (displays.contains(name)) {
                        return false;
                    }
                    if (i.getItemMeta().hasLore()) {
                        List<String> lores = legendaryGuild.getFileManager().getStores().getLore();
                        for (String lore : i.getItemMeta().getLore()) {
                            if (lores.contains(lore)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }

    }
}
