package com.legendaryrealms.LegendaryGuild.Command;

import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class LegendaryCommand {

    public final LegendaryGuild legendaryGuild = LegendaryGuild.getInstance();
    public final Lang lang = legendaryGuild.getFileManager().getLang();
    private String permission;
    private String command;
    private List<Integer> length;
    private boolean admin;

    public LegendaryCommand(String permission, String command, int length, boolean admin) {
        this.permission = permission;
        this.command = command;
        this.length = Collections.singletonList(length);
        this.admin = admin;
    }
    public LegendaryCommand(String permission, String command, List<Integer> length, boolean admin) {
        this.permission = permission;
        this.command = command;
        this.length = length;
        this.admin = admin;
    }
    public abstract void handle(CommandSender sender,String[] args);
    public abstract List<String> complete(CommandSender sender,String[] args);

    public String getPermission() {
        return permission;
    }

    public String getCommand() {
        return command;
    }

    public List<Integer> getLength() {
        return length;
    }

    public boolean isAdmin() {
        return admin;
    }

    public OfflinePlayer getPlayer(String name) {
        OfflinePlayer p = Bukkit.getPlayerExact(name);
        if (p == null) {
            // Not the best option, but Spigot doesn't offer a good replacement (as usual)
            p = Bukkit.getOfflinePlayer(name);
            return p.hasPlayedBefore() ? p : null;
        }
        return p;
    }


}
