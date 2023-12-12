package com.legendaryrealms.LegendaryGuild.Command.PlayerCommands;

import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class RedPacketCommand extends LegendaryCommand {
    public RedPacketCommand() {
        super("legendary.redpacket", "redpacket", 3, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String totalStr = args[1];
        String amountStr = args[2];
        if (sender instanceof Player){

            Player p = (Player) sender;
            if (!legendaryGuild.checkIsNumber(totalStr) || !legendaryGuild.checkIsNumber(amountStr)){
                sender.sendMessage(lang.plugin+lang.notmember);
                return;
            }
            double total = Double.parseDouble(totalStr);
            int amount = Integer.parseInt(amountStr);
            GuildAPI.createRedPacket(p,total,amount);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("总金额 (Total value)"),1,Arrays.asList("redpacket"),0)
                .addTab(Arrays.asList("红包份数 (Amount value)"),2,Arrays.asList("redpacket"),0)
                .build(args);
    }
}
