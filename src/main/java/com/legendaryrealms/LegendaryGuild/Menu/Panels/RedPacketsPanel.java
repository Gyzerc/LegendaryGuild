package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild_Redpacket;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Manager.Guild.GuildRedPacketsManager;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.RedPacketsLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class RedPacketsPanel extends MenuDraw {
    private int page;
    private final RedPacketsLoader loader = (RedPacketsLoader) getLoader();
    public RedPacketsPanel(Player p, int page) {
        super(p, "RedPackets");
        this.inv = Bukkit.createInventory(this,loader.getSize(),loader.getTitle());
        this.page = page;
        this.slotToRed = new HashMap<>();
        DrawEssentail(inv);
        load();
    }

    private HashMap<Integer,UUID> slotToRed;
    public void load(){
        LegendaryGuild.getInstance().sync(new Runnable() {
            @Override
            public void run() {
                User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
                Guild_Redpacket guild_redpacket=LegendaryGuild.getInstance().getDataBase().getRedPacket(user.getGuild());
                List<UUID> uids = getPage(user,guild_redpacket,page);
                if (uids.isEmpty()){
                    return;
                }
                int a = 0;
                for (UUID uuid : uids){
                    Guild_Redpacket.Redpacket redpacket = guild_redpacket.getRedpackets().get(uuid);
                    if (redpacket.getLess() <= 0) {
                        guild_redpacket.getRedpackets().remove(uuid);
                        LegendaryGuild.getInstance().getRedPacketsManager().updateRedPacket(guild_redpacket);
                        continue;
                    }

                    if (redpacket != null) {
                        ItemStack i = new ItemStack(loader.getPacket_icon_bef(), 1, (short) loader.getPacket_data_bef());
                        ItemMeta id = i.getItemMeta();
                        id.setDisplayName(loader.getPacket_display_bef());
                        List<String> lore = new ArrayList<>(loader.getPacket_lore_bef());
                        if (LegendaryGuild.getInstance().version_high) {
                            id.setCustomModelData(loader.getPacket_model_bef());
                        }
                        if (redpacket.getHistory().containsKey(p.getName())) {
                            i = new ItemStack(loader.getPacket_icon_after(), 1, (short) loader.getPacket_data_after());
                            id = i.getItemMeta();
                            id.setDisplayName(loader.getPacket_display_after());
                            lore = new ArrayList<>(loader.getPacket_lore_after());
                            if (LegendaryGuild.getInstance().version_high) {
                                id.setCustomModelData(loader.getPacket_model_after());
                            }
                        }

                        String luck = getPlaceHolder("luck_null");
                        double luck_value = 0.0;
                        GuildRedPacketsManager.RedPacketHistoryData first = LegendaryGuild.getInstance().getRedPacketsManager().getFirst(redpacket.getHistory());
                        if (first != null) {
                            luck = first.getPlayer();
                            luck_value = first.getAmount();
                        }

                        double finalLuck_value = luck_value;
                        String finalLuck = luck;
                        lore.replaceAll(l -> l
                                .replace("%owner%", redpacket.getPlayer())
                                .replace("%date%", redpacket.getDate())
                                .replace("%less%", redpacket.getLess() + "")
                                .replace("%total%", redpacket.getTotal() + "")
                                .replace("%amount%", redpacket.getAmount() + "")
                                .replace("%less_amount%", (redpacket.getAmount() - redpacket.getHistory().size()) + "")
                                .replace("%luck%", finalLuck)
                                .replace("%luck_value%", finalLuck_value + ""));
                        id.setLore(lore);
                        i.setItemMeta(id);
                        inv.setItem(getLayout().get(a), i);
                        slotToRed.put(getLayout().get(a), uuid);
                        a++;
                    }
                }
            }
        });

    }



    public List<UUID> getPage(User user,Guild_Redpacket guild_redpacket,int page){
        List<UUID> uids = new ArrayList<>();

        List<Guild_Redpacket.Redpacket> redpackets=guild_redpacket.getRedpackets().values().stream().filter(redpacket -> {return redpacket!=null;}).collect(Collectors.toList());



        int layout = getLayout().size();
        int start = 0 + (page-1)*layout;
        int end = layout + (page-1)*layout;

        for (int get = start ; get < end ; get++){
            if (redpackets.size() > get){
                Guild_Redpacket.Redpacket redpacket = redpackets.get(get);
                uids.add(redpacket.getUuid());
            }
        }
        return uids;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())){
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());
            Guild_Redpacket guild_redpacket=LegendaryGuild.getInstance().getDataBase().getRedPacket(user.getGuild());
            if (menuItem!=null){
                switch (menuItem.getFuction()){
                    case "pre" : {
                        if (page <= 1){
                            return;
                        }
                        RedPacketsPanel redPacketsPanel =new RedPacketsPanel(p,(page-1));
                        redPacketsPanel.load();
                        redPacketsPanel.open();
                        break;
                    }
                    case "next" : {
                        if (getPage(user,guild_redpacket,(page+1)).isEmpty()){
                            return;
                        }
                        RedPacketsPanel redPacketsPanel = new RedPacketsPanel(p,(page+1));
                        redPacketsPanel.load();
                        redPacketsPanel.open();
                        break;
                    }
                }
            }
            else {
                if (slotToRed.containsKey(e.getRawSlot())){
                    UUID uuid = slotToRed.get(e.getRawSlot());
                    guild_redpacket = LegendaryGuild.getInstance().getRedPacketsManager().getRedPacketData(user.getGuild());
                    Guild_Redpacket.Redpacket redpacket = guild_redpacket.getRedpackets().get(uuid);
                    if (redpacket == null){
                        p.sendMessage(lang.plugin+lang.redpacket_garb_no);
                        return;
                    }
                    if (LegendaryGuild.getInstance().getRedPacketsManager().grabRedPacket(user.getGuild(),uuid,p)){
                        RedPacketsPanel redPacketsPanel = new RedPacketsPanel(p,page);
                        redPacketsPanel.load();
                        redPacketsPanel.open();
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
