package com.legendaryrealms.LegendaryGuild.Hook.Sub;

import com.legendaryrealms.LegendaryGuild.Hook.Hook;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;

public class PlayerPointsHook extends Hook {
    private PlayerPoints pp;
    public PlayerPointsHook(LegendaryGuild legendaryGuild) {
        super(legendaryGuild);
        enable = getHook();
    }

    @Override
    public boolean getHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
            pp =  PlayerPoints.class.cast(Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints"));
            return true;
        }
        return false;
    }

    public PlayerPoints getPlayerPoints(){
        return pp;
    }
}
