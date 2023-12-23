package com.legendaryrealms.LegendaryGuild.Hook.Attribute;

import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Others.Buff;
import com.legendaryrealms.LegendaryGuild.Data.Others.StringStore;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MythicLib extends AttributePluginProvider{
    @Override
    public void updateBuff(Player p) {
        User user = UserAPI.getUser(p.getName());

        removePlayerStats(p,"LegendaryGuild_GuildBuff");
        removePlayerStats(p,"LegendaryGuild_GuildPosition");
        if (user.hasGuild()) {
            Guild guild = LegendaryGuild.getInstance().getGuildsManager().getGuild(user.getGuild());

            //公会buff属性
            List<String> GuildBuff_Attrs = new ArrayList<>();
            StringStore buffs = guild.getBuffs();
            for (String id : (List<String>) buffs.Values()) {
                Buff buff = LegendaryGuild.getInstance().getBuffsManager().getBuff(id).orElse(null);
                if (buff != null) {
                    int level = Integer.parseInt(buffs.getValue(id, 0).toString());
                    if (level > 0) {
                        GuildBuff_Attrs.addAll(buff.getAttr(level));
                    }
                }
            }
            MythicAttrRegister register = serializeAttr(GuildBuff_Attrs);
            register.apply(p,"LegendaryGuild_GuildBuff");

            //职位加成
            Position position = LegendaryGuild.getInstance().getPositionsManager().getPosition(user.getPosition()).orElse(LegendaryGuild.getInstance().getPositionsManager().getDefaultPosition());
            List<String> Position_Attrs = position.getAttrs();
            register = serializeAttr(Position_Attrs);
            register.apply(p,"LegendaryGuild_GuildPosition");
        }
    }

    private MythicAttrRegister serializeAttr(List<String> list){
        MythicAttrRegister mythicAttrRegister = new MythicAttrRegister();
        if (list != null && !list.isEmpty()) {
            for (String str : list) {
                String[] args = str.split(";");
                String name = getFromArray(args, 0, null);
                String value = getFromArray(args, 1, null);
                if (name != null && value != null){
                    mythicAttrRegister.addValue(name, Double.parseDouble(value));
                }
            }
        }
        return mythicAttrRegister;
    }

    private String getFromArray(String[] str,int pos,String def){
        if (str != null && str.length > pos) {
            return str[pos];
        }
        return def;
    }


    private void removePlayerStats(Player p,String source){
        MMOPlayerData playerData = MMOPlayerData.get(p.getUniqueId());
        StatMap statMap = playerData.getStatMap();
        for (StatInstance instance : statMap.getInstances()){
            instance.removeIf(i -> i.equals(source));
        }
    }

    private class MythicAttrRegister {

        private StringStore attrs;

        public MythicAttrRegister() {
            this.attrs = new StringStore();
        }

        public void addValue(String key,double value){
            double old = (double) attrs.getValue(key,0.0);
            attrs.setValue(key,(old+value),value);
        }

        public StringStore getAttrs() {
            return attrs;
        }

        public void apply(Player p,String source){
            MMOPlayerData playerData = MMOPlayerData.get(p.getUniqueId());
            for (String attrName : (List<String>)attrs.Values()){
                StatModifier experienceModifier = new StatModifier(source, attrName, Double.parseDouble(attrs.getValue(attrName,0.0).toString()), ModifierType.FLAT);
                experienceModifier.register(playerData);
            }
        }
    }
}
