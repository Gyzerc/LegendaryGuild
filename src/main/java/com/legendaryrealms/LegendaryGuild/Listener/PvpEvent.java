package com.legendaryrealms.LegendaryGuild.Listener;

import com.legendaryrealms.LegendaryGuild.API.Events.PlayerDamagedBySameGuildMemberEvent;
import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpEvent implements Listener {
    private final Lang lang = LegendaryGuild.getInstance().getFileManager().getLang();
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.isCancelled()){return;}
        if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow){
            Player damager = null;
            if(e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            }
            else {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player){
                    damager = (Player) arrow.getShooter();
                }
            }
            if (damager == null ) {
                return;
            }
            User DamageUser = UserAPI.getUser(damager.getName());
            if (DamageUser.hasGuild() && e.getEntity() instanceof Player){
                Player p = (Player) e.getEntity();
                if (UserAPI.hasGuild(p.getName()) ) {
                    User entityUser = UserAPI.getUser(p.getName());
                    if (entityUser.getPvp().equals(User.PvpType.NO_SAME_GUILD) && entityUser.getGuild().equals(DamageUser.getGuild())) {
                        e.setCancelled(true);
                        damager.sendMessage(lang.plugin + lang.pvp_cant);
                        Bukkit.getPluginManager().callEvent(new PlayerDamagedBySameGuildMemberEvent(p, damager, true));
                    }
                }
            }
            return;
        }

        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            User user = UserAPI.getUser(p.getName());
            if (user.hasGuild()){
                Player damager = null;
                if (e.getDamager() instanceof  Player){
                    damager = (Player) e.getDamager();
                }
                if (e.getDamager() instanceof Arrow){
                    Arrow arrow = (Arrow) e.getDamager();
                    if (arrow.getShooter() instanceof Player){
                        damager = (Player) arrow.getShooter();
                    }
                }
                if (damager != null){
                    User DamagerUser = UserAPI.getUser(damager.getName());
                    if (DamagerUser.hasGuild()){
                        //当受伤一方开启公会保护时 且 攻击者是同一个公会的
                        if (user.getPvp().equals(User.PvpType.NO_SAME_GUILD) && DamagerUser.getGuild().equals(user.getGuild())){
                            e.setCancelled(true);
                            damager.sendMessage(lang.plugin+lang.pvp_cant_target);
                            Bukkit.getPluginManager().callEvent(new PlayerDamagedBySameGuildMemberEvent(p,damager,true));
                            return;
                        }
                    }
                }

            }
        }
    }
}
