package com.legendaryrealms.LegendaryGuild.Manager;

import com.legendaryrealms.LegendaryGuild.Hook.Sub.PlayerPointsHook;
import com.legendaryrealms.LegendaryGuild.Hook.Sub.VaultHook;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;

public class HookManager {

    private LegendaryGuild legendaryGuild;
    private VaultHook vaultHook;
    private PlayerPointsHook playerPointsHook;
    public HookManager(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;

        this.vaultHook = new VaultHook(legendaryGuild);
        this.playerPointsHook = new PlayerPointsHook(legendaryGuild);
    }
    public VaultHook getVaultHook(){return vaultHook;}

    public PlayerPointsHook getPlayerPointsHook() {
        return playerPointsHook;
    }
}
