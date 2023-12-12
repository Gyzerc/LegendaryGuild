package com.legendaryrealms.LegendaryGuild.Requirement.Sub;

import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GuildPositionRequirement extends Requirement {
    @Override
    public String getSymbol() {
        return "guild_position";
    }

    @Override
    public boolean canPass(Player p, String str) {
        User user = legendaryGuild.getUsersManager().getUser(p.getName());
        if (!user.hasGuild()){
            p.sendMessage(lang.plugin+lang.nothasguild);
            return false;
        }

        String[] args = str.split(";");
        String positions = getFromArray(args,0,"");
        List<Position> positionList=new ArrayList<>();
        if (positions.contains(",")){
            for (String id : positions.split(",")){
                Position readPosition = getPosition(id);
                if (readPosition != null){
                    positionList.add(readPosition);
                }
            }
        }

        Position playerPosition = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition());
        if (positionList.contains(playerPosition)){
            return true;
        }
        p.sendMessage(lang.plugin+lang.nopass_position);
        return false;
    }


    private Position getPosition(String id){
       Position position = legendaryGuild.getPositionsManager().getPosition(id).orElse(null);
       if (position != null){
           return position;
       }
       legendaryGuild.info("职位id不存在！->"+id,Level.SEVERE);
       return null;

    }
    @Override
    public void deal(Player p, String str) {

    }
}
