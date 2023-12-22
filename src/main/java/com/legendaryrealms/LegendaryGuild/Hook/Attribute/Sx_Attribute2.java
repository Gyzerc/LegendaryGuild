package com.legendaryrealms.LegendaryGuild.Hook.Attribute;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Sx_Attribute2 extends AttributePluginProvider{
    @Override
    public void updateBuff(Player p) {

        User user = UserAPI.getUser(p.getName());

        SXAttribute.getApi().removeEntityAPIData(LegendaryGuild.class,p.getUniqueId());
        if (user.hasGuild()) {
            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());

            //公会buff 以及 职位BUff 属性
            List<String> Attrs = new ArrayList<>();

            //公会buff
            StringStore buffs = guild.getBuffs();
            for (String id : (List<String>) buffs.Values()) {
                Buff buff = LegendaryGuild.getInstance().getBuffsManager().getBuff(id).orElse(null);
                if (buff != null) {
                    int level = Integer.parseInt(buffs.getValue(id, 0).toString());
                    if (level > 0) {
                        Attrs.addAll(buff.getAttr(level));
                    }
                }
            }

            //职位buff
            Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(user.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
            List<String> Position_Attrs = position.getAttrs();
            Attrs.addAll(Position_Attrs);

            SXAttributeData data = SXAttribute.getApi().getLoreData(null, null, Attrs);
            SXAttribute.getApi().setEntityAPIData(LegendaryGuild.class, p.getUniqueId(), data);
        }
    }
}
