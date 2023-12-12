package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p =e.getPlayer();
        User user = LegendaryGuild.getInstance().getUsersManager().getUser(p.getName());

        //更新数据
        LegendaryGuild.getInstance().getUsersManager().updateUser(user,true);

    }
}
