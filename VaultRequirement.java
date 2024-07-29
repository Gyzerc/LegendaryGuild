package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class VaultRequirement extends Requirement {
    private String SYMBOL = "vault";

    public boolean canPass(Player p,String str){
        if (!legendaryGuild.getHookManager().getVaultHook().isEnable()){
            legendaryGuild.info("检测到服务器未安装 Vault .",Level.SEVERE);
            legendaryGuild.info("Please install Vault to use 'vault;XXX'.",Level.SEVERE);
            return false;
        }
        try {
            String[] args=str.split(";");
            double price = Double.parseDouble(args[1]);
            if (legendaryGuild.getHookManager().getVaultHook().getEconomy().getBalance(p) >= price){
                return true;
            }
            p.sendMessage(lang.plugin+lang.reuirement_notenough_vault.replace("%value%",price+""));
            return false;
        } catch (ClassCastException e){
            legendaryGuild.info("出现错误 -> "+str +" 值必须为double类型！", Level.SEVERE,e);
            return false;
        } catch (NullPointerException e){
            legendaryGuild.info("出现错误 -> "+str +" 缺少参数！请确认写法正确！", Level.SEVERE,e);
            return false;
        }
    }

    @Override
    public void deal(Player p, String str) {
        if (!legendaryGuild.getHookManager().getVaultHook().isEnable()){
            return;
        }
        try {
            String[] args=str.split(";");
            double price = Double.parseDouble(args[1]);
            legendaryGuild.getHookManager().getVaultHook().getEconomy().withdrawPlayer(p,price);
            return;
        } catch (ClassCastException e){
            legendaryGuild.info("出现错误 -> "+str +" 值必须为double类型！", Level.SEVERE,e);
            return;
        } catch (NullPointerException e){
            legendaryGuild.info("出现错误 -> "+str +" 缺少参数！请确认写法正确！", Level.SEVERE,e);
            return;
        }
    }

    @Override
    public String getSymbol() {
        return SYMBOL;
    }
}
