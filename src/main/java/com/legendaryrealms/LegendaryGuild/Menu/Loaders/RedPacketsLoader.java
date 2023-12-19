package com.legendaryrealms.LegendaryGuild.Menu.Loaders;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.MenuLoader;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RedPacketsLoader extends MenuLoader {
    private Material packet_icon_bef;
    private String packet_display_bef;
    private int packet_data_bef;
    private int packet_model_bef;
    private List<String> packet_lore_bef;
    private Material packet_icon_after;
    private String packet_display_after;
    private int packet_data_after;
    private int packet_model_after;
    private List<String> packet_lore_after;
    public RedPacketsLoader(LegendaryGuild legendaryGuild) {
        super(legendaryGuild,"./plugins/LegendaryGuild/Contents/Menus/"+legendaryGuild.lang.name(), "Contents/Menus/"+legendaryGuild.lang.name()+"/","RedPackets.yml");
    }

    @Override
    protected void readDefault() {
        packet_icon_bef = getMaterial(getValue("redpacket_item_before.material","STONE"));
        packet_data_bef = getValue("redpacket_item_before.data",0);
        packet_model_bef = getValue("redpacket_item_before.model",0);
        packet_display_bef = legendaryGuild.color(getValue("redpacket_item_before.display","%player%"));
        packet_lore_bef = legendaryGuild.color(getValue("redpacket_item_before.lore",new ArrayList<>()));

        packet_icon_after = getMaterial(getValue("redpacket_item_after.material","STONE"));
        packet_data_after = getValue("redpacket_item_after.data",0);
        packet_model_after = getValue("redpacket_item_after.model",0);
        packet_display_after = legendaryGuild.color(getValue("redpacket_item_after.display","%player%"));
        packet_lore_after = legendaryGuild.color(getValue("redpacket_item_after.lore",new ArrayList<>()));
    }

    public Material getPacket_icon_bef() {
        return packet_icon_bef;
    }

    public String getPacket_display_bef() {
        return packet_display_bef;
    }

    public int getPacket_data_bef() {
        return packet_data_bef;
    }

    public int getPacket_model_bef() {
        return packet_model_bef;
    }

    public List<String> getPacket_lore_bef() {
        return packet_lore_bef;
    }

    public Material getPacket_icon_after() {
        return packet_icon_after;
    }

    public String getPacket_display_after() {
        return packet_display_after;
    }

    public int getPacket_data_after() {
        return packet_data_after;
    }

    public int getPacket_model_after() {
        return packet_model_after;
    }

    public List<String> getPacket_lore_after() {
        return packet_lore_after;
    }
}
