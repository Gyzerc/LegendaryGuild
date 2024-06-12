package com.legendaryrealms.LegendaryGuild.Command;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Command.AdminCommands.*;
import com.legendaryrealms.LegendaryGuild.Command.AdminCommands.MoneyCommand;
import com.legendaryrealms.LegendaryGuild.Command.PlayerCommands.*;
import com.legendaryrealms.LegendaryGuild.Command.PlayerCommands.DeleteCommand;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Listener.Custom.NewCycleEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {


    private static final LegendaryGuild legendaryguild=LegendaryGuild.getInstance();
    private static final Lang lang=legendaryguild.getFileManager().getLang();
    private static LinkedHashMap<String,LegendaryCommand> commands;
    private static HashMap<String,LegendaryCommand> admin_commands;
    public static void register(){
        commands = new LinkedHashMap<>();
        admin_commands = new HashMap<>();

        commands.put("list",new ListCommand());
        commands.put("open",new OpenCommand());
        commands.put("join",new JoinCommand());
        commands.put("create",new CreateGuildCommand());
        commands.put("money",new com.legendaryrealms.LegendaryGuild.Command.PlayerCommands.MoneyCommand());
        commands.put("position",new PositionCommand());
        commands.put("redpacket",new RedPacketCommand());
        commands.put("chat",new ChatCommand());
        commands.put("pvp",new PvpCommand());
        commands.put("menu",new MenuCommand());
        commands.put("quit",new QuitCommand());
        commands.put("give",new GiveCommand());
        commands.put("delete",new DeleteCommand());
        commands.put("admin",new AdminCommand());

        admin_commands.put("maxmembers",new MaxMembersCommand());
        admin_commands.put("icon",new IconCommand());
        admin_commands.put("points",new PointsCommand());
        admin_commands.put("reload",new ReloadCommand());
        admin_commands.put("money",new MoneyCommand());
        admin_commands.put("level",new LevelCommand());
        admin_commands.put("exp",new ExpCommand());
        admin_commands.put("treelevel",new TreeLevelCommand());
        admin_commands.put("treeexp",new TreeExpCommand());
        admin_commands.put("buff",new BuffCommand());
        admin_commands.put("activity",new ActivityCommand());
        admin_commands.put("delete",new com.legendaryrealms.LegendaryGuild.Command.AdminCommands.DeleteCommand());
        admin_commands.put("reset",new ResetCommand());
        admin_commands.put("teamshop",new TeamShopCommand());

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {
        int length = strings.length;
        if (length == 0){
            //发送指令提示
            lang.help_player.forEach(msg -> {
                sender.sendMessage(msg);
            });
        }
        else {
            String subCommandName = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            if (subCommandName.equals("admin") && length >1){
                map = admin_commands;
                subCommandName = strings[1];
            }
            LegendaryCommand cmd = map.get(subCommandName);
            if (cmd == null){
                sender.sendMessage(lang.plugin+lang.unknown_command);
                return false;
            }
            if (sender.hasPermission(cmd.getPermission())) {
                if (cmd.getLength().contains(length)) {
                    cmd.handle(sender, strings);
                    return true;
                }
            }
            else {
                sender.sendMessage(lang.plugin + lang.nopermission);
                return true;
            }
            sender.sendMessage(lang.plugin+lang.unknown_command);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        int length = strings.length;
        List<String> tab=new ArrayList<>();
        if (length == 1 ){
            for (Map.Entry<String,LegendaryCommand> entry:commands.entrySet()){
                LegendaryCommand legendaryCommand=entry.getValue();
                if ((legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    tab.add(entry.getKey());
                }
            }
            return tab;
        }
        else {
            String subCommand = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            if (subCommand.equals("admin")){
                map = admin_commands;
                if (length == 2){
                    map.forEach((s1, legendaryCommand) -> {
                        tab.add(s1);
                    });
                    return tab;
                }
                subCommand = strings[1];
            }
            LegendaryCommand legendaryCommand = map.get(subCommand);
            if (legendaryCommand != null){
                if ((legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    return legendaryCommand.complete(commandSender,strings);
                }
            }
        }
        return null;
    }
}
