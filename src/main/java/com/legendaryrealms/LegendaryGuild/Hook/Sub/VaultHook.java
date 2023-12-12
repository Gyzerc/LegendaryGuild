package com.legendaryrealms.LegendaryGuild.Hook.Sub;

import com.legendaryrealms.LegendaryGuild.Hook.Hook;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook extends Hook {
    private Economy economy;
    public VaultHook(LegendaryGuild legendaryGuild){
        super(legendaryGuild);
        enable = getHook();
    }
    @Override
    public boolean getHook() {
        RegisteredServiceProvider<Economy> rsp = legendaryGuild.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Economy getEconomy(){
        return economy;
    }
}
