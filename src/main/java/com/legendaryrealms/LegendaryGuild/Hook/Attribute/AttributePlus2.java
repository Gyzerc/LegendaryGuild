package com.legendaryrealms.LegendaryGuild.Hook.Attribute;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;
import org.serverct.ersha.jd.AttributeAPI;

import java.util.ArrayList;
import java.util.List;

public class AttributePlus2 extends AttributePluginProvider{
    @Override
    public void updateBuff(Player p) {
        User user = UserAPI.getUser(p.getName());

        AttributeAPI.deleteAttribute(p,"LegendaryGuild_GuildBuff");
        AttributeAPI.deleteAttribute(p,"LegendaryGuild_GuildPosition");

        if (user.hasGuild()){
            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());

            //公会buff属性
            List<String> GuildBuff_Attrs = new ArrayList<>();
            StringStore buffs = guild.getBuffs();
            for (String id : (List<String>)buffs.Values()){
                Buff buff = LegendaryGuild.getInstance().getBuffsManager().getBuff(id).orElse(null);
                if (buff != null){
                    int level  = Integer.parseInt(buffs.getValue(id,0).toString()) ;
                    if (level > 0 ){
                        GuildBuff_Attrs.addAll(buff.getAttr(level));
                    }
                }
            }
            AttributeAPI.addAttribute(p,"LegendaryGuild_GuildBuff",GuildBuff_Attrs,false);

            //职位加成
            Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(user.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
            List<String> Position_Attrs = position.getAttrs();
            AttributeAPI.addAttribute(p,"LegendaryGuild_GuildPosition",Position_Attrs,false);

            return;
        }
    }
}
