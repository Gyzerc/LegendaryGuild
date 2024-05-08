package com.legendaryrealms.LegendaryGuild.Menu.Panels;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Loaders.PositionsLoader;
import com.legendaryrealms.LegendaryGuild.Menu.MenuDraw;
import com.legendaryrealms.LegendaryGuild.Menu.MenuItem;
import com.legendaryrealms.LegendaryGuild.Utils.ReplaceHolderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PositionsPanel extends MenuDraw {
    public PositionsPanel(Player p) {
        super(p, "Positions");
        this.inv = Bukkit.createInventory(this,getLoader().getSize(),getLoader().getTitle());

        Guild guild = UserAPI.getGuild(p.getName()).orElse(null);

        DrawEssentailSpecial(inv,menuItem -> {
            if (guild != null) {

                if (menuItem.getFuction().equals("position")) {
                    String positionId = menuItem.getValue();
                    Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(positionId).orElse(null);
                    if (position != null) {

                        List<String> list = guild.getMembers().stream().filter(m -> {
                            User user = UserAPI.getUser(m);
                            return user.getPosition().equals(positionId);
                        }).collect(Collectors.toList());


                        ItemStack i = menuItem.getI();

                        ReplaceHolderUtils replaceHolderUtils = new ReplaceHolderUtils()
                                .addSinglePlaceHolder("display",position.getDisplay())
                                .addSinglePlaceHolder("amount",list.size()+"")
                                .addListPlaceHolder("members",list);


                        menuItem.setI(replaceHolderUtils.startReplace(i,false,null));


                    }
                }
            }
        });
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!dealEssentailsButton(e.getRawSlot())) {
            PositionsLoader loader = (PositionsLoader) getLoader();
            MenuItem menuItem = loader.getMenuItem(e.getRawSlot());
            if (menuItem != null) {
                switch (menuItem.getFuction()) {
                    case "position": {
                        Guild guild = UserAPI.getGuild(p.getName()).orElse(null);
                        if (guild != null && guild.getOwner().equals(p.getName())) {
                            String positionId = menuItem.getValue();
                            Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(positionId).orElse(null);
                            if (position != null) {
                                if (e.isLeftClick()) {
                                    p.closeInventory();
                                    p.sendMessage(lang.plugin + lang.positions_add_write.replace("%position%",position.getDisplay()));
                                    LegendaryGuild.getInstance().getChatControl().setModify(p.getUniqueId(),2,positionId);
                                    //                                    new AnvilGUI.Builder().title(lang.positions_add_write)
//                                            .text(
//                                                    "...."
//                                            )
//                                            .plugin(LegendaryGuild.getInstance())
//                                            .onClick((slot, stateSnapshot) -> {
//                                                        if (slot != AnvilGUI.Slot.OUTPUT) {
//                                                            return Collections.emptyList();
//                                                        }
//                                                        String target = stateSnapshot.getText();
//                                                        UserAPI.setPlayerPositionByPlayer(p,target,positionId);
//                                                        return  Arrays.asList(AnvilGUI.ResponseAction.close());
//                                                    }).open(p);
                                    return;
                                }
                                if (e.isRightClick()){
                                    p.closeInventory();
                                    p.sendMessage(lang.positions_remove_write.replace("%position%",position.getDisplay()));
                                    LegendaryGuild.getInstance().getChatControl().setModify(p.getUniqueId(),3);
//                                    new AnvilGUI.Builder().title())
//                                            .text(
//                                                    "...."
//                                            )
//                                            .plugin(LegendaryGuild.getInstance())
//                                            .onClick((slot, stateSnapshot) -> {
//                                                if (slot != AnvilGUI.Slot.OUTPUT) {
//                                                    return Collections.emptyList();
//                                                }
//                                                String target = stateSnapshot.getText();
//                                                UserAPI.removePlayerPosition(p,target);
//                                                return  Arrays.asList(AnvilGUI.ResponseAction.close());
//                                            }).open(p);
                                    return;
                                }
                            }
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
