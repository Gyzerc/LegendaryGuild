package com.legendaryrealms.LegendaryGuild.Hook.Attribute;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;
import org.serverct.ersha.AttributePlus;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeData;

import java.util.ArrayList;
import java.util.List;

public class AttributePlus3 extends AttributePluginProvider{
    @Override
    public void updataBuff(Player p) {
        User user = UserAPI.getUser(p.getName());
        AttributeData data = AttributePlus.attributeManager.getAttributeData(p);
        data.takeApiAttribute("LegendaryGuild_GuildBuff");
        if (user.hasGuild()){
            List<String> Attrs = new ArrayList<>();
            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());
            StringStore buffs = guild.getBuffs();
            for (String id : (List<String>)buffs.Values()){
                Buff buff = LegendaryGuild.getInstance().getBuffsManager().getBuff(id).orElse(null);
                if (buff != null){
                    int level  = Integer.parseInt(buffs.getValue(id,0).toString()) ;
                    if (level > 0 ){
                        Attrs.addAll(buff.getAttr(level));
                    }
                }
            }
            AttributeAPI.addSourceAttribute(data,"LegendaryGuild_GuildBuff",Attrs,true);
            return;
        }
    }
}
