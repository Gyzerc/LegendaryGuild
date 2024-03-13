package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class MoveEvent implements Listener {
    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private final Lang lang = legendaryGuild.getFileManager().getLang();
    private final Config config = legendaryGuild.getFileManager().getConfig();
    private static List<String> waiting_teleports;

    public MoveEvent() {
        this.waiting_teleports = new ArrayList<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location newLoc = e.getTo();
        Location form = e.getFrom();


        //检测是否移动
        if (newLoc.getBlockX() != form.getBlockX() || newLoc.getBlockY() != form.getBlockY() || newLoc.getBlockZ() != form.getBlockZ()){
            //移除是待传送名单并发送消息
            if (deletePlayerWaitTeleport(p.getName())) {
                if (config.HOME_SOUND_CANCEL != null) {
                    p.playSound(p.getLocation(), config.HOME_SOUND_CANCEL, 1, 1);
                }
                p.sendMessage(lang.plugin+lang.home_cancel);
            }
        }
    }

    public static void addPlayerWaitTeleport(String player){
        if (waiting_teleports.contains(player)){
            return;
        }
        waiting_teleports.add(player);
    }

    public static boolean hasPlayerWaitTeleport(String player){
        return waiting_teleports.contains(player);
    }

    public static boolean deletePlayerWaitTeleport(String player){
        return waiting_teleports.remove(player);
    }
}
