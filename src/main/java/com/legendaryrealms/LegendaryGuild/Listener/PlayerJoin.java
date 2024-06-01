package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Files.Config;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerJoin implements Listener {
    private final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    private final Lang lang = legendaryGuild.getFileManager().getLang();
    private final Config config = legendaryGuild.getFileManager().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player p=e.getPlayer();

        //防止跨服模式下数据未及时更新
        if (legendaryGuild.getNetWork().isEnable()) {
            if (Bukkit.getOnlinePlayers().size() == 1) {
                legendaryGuild.reloadData();
            }
        }

        //初始化数据
        legendaryGuild.getUsersManager().reloadUserDataIfCached(p.getName());

        //检测是否是会长已经发送入会申请待处理消息
        User user = legendaryGuild.getUsersManager().getUser(p.getName());
        if (user.hasGuild()){
            if (user.getPosition().equals(legendaryGuild.getPositionsManager().getOwnerPosition().getId())){
                Guild guild = legendaryGuild.getGuildsManager().getGuild(user.getGuild());
                if (guild.getApplications().size() > 0){
                    Bukkit.getScheduler().runTaskLaterAsynchronously(legendaryGuild, new Runnable() {
                        @Override
                        public void run() {
                            p.sendMessage(lang.plugin+lang.application_wait.replace("%value%",""+guild.getApplications().size()));
                        }
                    },100);
                }
            }
        }

        //刷新公会buff属性
        UserAPI.updatePlayerBuffAttribute(p);

        //检测是否是要传送至驻地
        Bukkit.getScheduler().runTaskLater(legendaryGuild,()->{
            if (user.isTeleport_guild_home()){
                Guild guild = UserAPI.getGuild(p.getName()).orElse(null);
                if (guild != null && guild.getHome()!=null){
                    if (guild.getHome().getServer().equals(legendaryGuild.SERVER)) {
                        user.setTeleport_guild_home(false);
                        user.update(false);

                        Location location = guild.getHome().getLocation().orElse(null);
                        if (location != null) {
                            p.teleport(location);
                            if (config.HOME_SOUND_TELEPORT != null) {
                                p.playSound(p.getLocation(), config.HOME_SOUND_TELEPORT, 1, 1);
                            }
                            p.sendMessage(lang.plugin + lang.home_teleport);
                        }
                    }
                }
            }
        },20);
    }


}
