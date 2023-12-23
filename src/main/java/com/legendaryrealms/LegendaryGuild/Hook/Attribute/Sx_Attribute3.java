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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sx_Attribute3 extends AttributePluginProvider{
    private Object SxApi;
    private Method setData;
    private Method loadAttrs;
    private Method removeData;

    public Sx_Attribute3(){
        try {
            Class<?> c = Class.forName("github.saukiya.sxattribute.SXAttribute");
            Method get = c.getMethod("getApi");
            SxApi = get.invoke(c);
            setData = SxApi.getClass().getMethod("setEntityAPIData", Class.class, UUID.class, SXAttributeData.class);
            loadAttrs = SxApi.getClass().getMethod("loadListData", List.class);
            removeData = SxApi.getClass().getMethod("removeEntityAPIData", Class.class, UUID.class);
            System.out.println("初始化成功！");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void updateBuff(Player p) {
        User user = UserAPI.getUser(p.getName());

        removeAttribute(p.getUniqueId());
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


            //更新属性
            setAttribute(p.getUniqueId(),Attrs);
        }
    }

    private void removeAttribute(UUID uuid){
        try {
            removeData.invoke(SxApi,LegendaryGuild.class,uuid);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAttribute(UUID uuid,List<String> attrs){
        try {
            SXAttributeData data = (SXAttributeData) loadAttrs.invoke(SxApi,attrs);
            setData.invoke(SxApi,LegendaryGuild.class,uuid,data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
