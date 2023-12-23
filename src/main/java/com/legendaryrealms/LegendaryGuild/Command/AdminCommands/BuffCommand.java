package com.legendaryrealms.LegendaryGuild.Command.AdminCommands;

import com.legendaryrealms.LegendaryGuild.API.Events.GuildBuffLevelupEvent;
import com.legendaryrealms.LegendaryGuild.API.GuildAPI;
import com.legendaryrealms.LegendaryGuild.Command.CommandTabBuilder;
import com.legendaryrealms.LegendaryGuild.Command.LegendaryCommand;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class BuffCommand extends LegendaryCommand {
    public BuffCommand() {
        super("LegendaryGuild.admin", "buff", 6, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String aot = args[2];
        String guildName = args[3];
        String buffId = args[4];
        String amountStr = args[5];
        if (!legendaryGuild.checkIsNumber(amountStr)){
            sender.sendMessage(lang.plugin+lang.notmath);
            return;
        }
        int amount = Integer.parseInt(amountStr);
        if (!legendaryGuild.getGuildsManager().isExists(guildName)){
            sender.sendMessage(lang.plugin+lang.notguild);
            return;
        }
        Guild guild = legendaryGuild.getGuildsManager().getGuild(guildName);

        Buff buff = legendaryGuild.getBuffsManager().getBuff(buffId).orElse(null);
        if (buff == null){
            sender.sendMessage(lang.plugin+lang.admin_buff_null);
            return;
        }

        StringStore data = guild.getBuffs();
        int current = Integer.parseInt(data.getValue(buffId,0).toString());


        switch (aot.toLowerCase()) {
            case "add": {
                int add = current + amount <= buff.getMax() ? amount : buff.getMax()-current;

                if (add > 0) {
                    data.setValue(buffId, (current + add), 0);
                    guild.setBuffs(data);
                    guild.update();
                    //更新buff属性
                    GuildAPI.updateGuildMembersBuff(guild);
                    legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(), lang.plugin + lang.buff_levelup.replace("%target", buff.getDisplay()).replace("%value%", (current + add) + ""));
                }
                sender.sendMessage(lang.plugin+lang.admin_bufflevel_add.replace("%target%",guildName).replace("%buff%",buff.getDisplay()).replace("%value%",add+""));

                Bukkit.getPluginManager().callEvent(new GuildBuffLevelupEvent(guild,buff,add));
                break;
            }
            case "take": {
                int take = current - amount >= 0 ? amount : current;
                if (take > 0){
                    data.setValue(buffId,(current-take),0);
                    guild.setBuffs(data);
                    guild.update();
                    //更新buff属性
                    // GuildAPI.updateGuildMembersBuff(guild);
                }
                sender.sendMessage(lang.plugin+lang.admin_bufflevel_take.replace("%target%",guildName).replace("%buff%",buff.getDisplay()).replace("%value%",take+""));
                break;
            }
            case "set": {
                int set = Math.min(amount, buff.getMax());
                data.setValue(buffId,set,0);
                guild.setBuffs(data);
                guild.update();
                //更新buff属性
                GuildAPI.updateGuildMembersBuff(guild);
                sender.sendMessage(lang.plugin+lang.admin_bufflevel_set.replace("%target%",guildName).replace("%buff%",buff.getDisplay()).replace("%value%",set+""));
                break;
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","set","take"),2,Arrays.asList("buff"),1)
                .addTab(legendaryGuild.getGuildsManager().getGuilds(),3,Arrays.asList("add","take","set"),2)
                .addTab(legendaryGuild.getBuffsManager().getBuffs().stream().map(b -> b.getId()).collect(Collectors.toList()), 4,Arrays.asList("add","take","set"),2)
                .addTab(Arrays.asList("Amount(数量)"),5,Arrays.asList("add","take","set"),2)
                .build(args);
    }
}
