package com.legendaryrealms.LegendaryGuild.Listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.legendaryrealms.LegendaryGuild.API.UserAPI;
import com.legendaryrealms.LegendaryGuild.Data.Guild.Guild;
import com.legendaryrealms.LegendaryGuild.Data.Guild.GuildStore;
import com.legendaryrealms.LegendaryGuild.Data.User.Position;
import com.legendaryrealms.LegendaryGuild.Data.User.User;
import com.legendaryrealms.LegendaryGuild.Files.Lang;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import com.legendaryrealms.LegendaryGuild.Menu.Panels.GuildMenuPanel;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessage;
import com.legendaryrealms.LegendaryGuild.Utils.BungeeCord.NetWorkMessageBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ChatEvent {
    private HashMap<UUID,Input> modify;
    private LegendaryGuild legendaryGuild;
    private Lang lang = LegendaryGuild.getInstance().getFileManager().getLang();

    public ChatEvent(LegendaryGuild legendaryGuild) {
        this.legendaryGuild = legendaryGuild;
        modify = new HashMap<>();
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            register();
            return;
        }
        legendaryGuild.info("未检测到ProtocolLib, 请安装该插件！ & ProtocolLib not detected, please install the plugin", Level.SEVERE);
    }
    public void setModify(UUID uuid,int id) {
        modify.put(uuid,new Input(id,""));
    }
    public void setModify(UUID uuid,int id,String value) {
        modify.put(uuid,new Input(id,value));
    }
    public void removeModify(UUID uuid) {
        modify.remove(uuid);
    }
    public void register() {
        //公会聊天
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(legendaryGuild, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                User user = UserAPI.getUser(p.getName());
                PacketContainer container = event.getPacket();
                String str = container.getStrings().read(0);
                if (user.hasGuild()) {
                    UUID uuid = p.getUniqueId();
                    //编辑公会
                    if (modify.containsKey(uuid)) {
                        event.setCancelled(true);
                        Input input = modify.remove(uuid);
                        int mode = input.getId();
                        String value = input.getValue();
                        if (!str.equalsIgnoreCase("cancel")) {
                            Guild guild = UserAPI.getGuild(p.getName()).get();
                            String target = LegendaryGuild.getInstance().color(str);
                            switch (mode) {
                                case 0 :
                                    List<String> intro = new ArrayList<>(guild.getIntro());
                                    if (intro.size() < LegendaryGuild.getInstance().getFileManager().getConfig().DESC_MAX_LENGTH) {
                                        intro.add(target);
                                        guild.setIntro(intro);
                                        guild.update();
                                        p.sendMessage(lang.plugin + lang.intro_add.replace("%value%", target));
                                        return;
                                    }
                                    p.sendMessage(lang.plugin + lang.max_length_intro);
                                    break;
                                case 1:
                                    List<String> notice = new ArrayList<>(guild.getNotice());
                                    if (notice.size() < LegendaryGuild.getInstance().getFileManager().getConfig().NOTICE_MAX_LENGTH) {
                                        notice.add(target);
                                        guild.setNotice(notice);
                                        guild.update();
                                        p.sendMessage(lang.plugin + lang.notice_add.replace("%value%", target));
                                        return;
                                    }
                                    p.sendMessage(lang.plugin+lang.max_length_notice);
                                    break;
                                case 2:
                                    UserAPI.setPlayerPositionByPlayer(p,target,value);
                                    return;
                                case 3:
                                    UserAPI.removePlayerPosition(p,target);
                                    return;
                                case 4:
                                    GuildStore store = legendaryGuild.getStoresManager().getStore(guild.getGuild());
                                    int id = Integer.parseInt(value);
                                    GuildStore.StoreData data = store.getData(id);
                                    if (data.getWhite().contains(target) || target.equals(p.getName())) {
                                        p.sendMessage(lang.plugin+lang.stores_add_white_already);
                                        return;
                                    }
                                    if (!guild.getMembers().contains(target)) {
                                        p.sendMessage(lang.plugin + lang.notmember);
                                        return;
                                    }
                                    p.sendMessage(lang.plugin+lang.stores_add_white.replace("%target%",target).replace("%value%",""+id));
                                    List<String> white = new ArrayList<>(data.getWhite());
                                    white.add(target);
                                    store.setWhite(id,white);
                                    //更新数据库
                                    legendaryGuild.getStoresManager().update(store);
                                    //通知其他子服务器
                                    new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                                            .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_STORE, guild.getGuild()))
                                            .setReciver("ALL")
                                            .sendPluginMessage(p);
                                    return;
                                case 5:
                                    store = legendaryGuild.getStoresManager().getStore(guild.getGuild());
                                    id = Integer.parseInt(value);
                                    data = store.getData(id);
                                    if (!data.getWhite().contains(target)) {
                                        p.sendMessage(lang.plugin+lang.stores_remove_white_null);
                                        return;
                                    }
                                    if (!guild.getMembers().contains(target)) {
                                        p.sendMessage(lang.plugin + lang.notmember);
                                        return;
                                    }
                                    p.sendMessage(lang.plugin+lang.stores_remove_white.replace("%target%",target).replace("%value%",""+id));
                                    white= new ArrayList<>(data.getWhite());
                                    white.remove(target);
                                    store.setWhite(id,white);
                                    //更新数据库
                                    legendaryGuild.getStoresManager().update(store);
                                    //通知其他子服务器
                                    new NetWorkMessageBuilder().setMessageType(NetWorkMessageBuilder.MessageType.Forward)
                                            .setNetWorkMessage(new NetWorkMessage(NetWorkMessage.NetWorkType.UPDATE_STORE, guild.getGuild()))
                                            .setReciver("ALL")
                                            .sendPluginMessage(p);
                                    return;
                            }
                        }
                        GuildMenuPanel menuPanel = new GuildMenuPanel(p);
                        LegendaryGuild.getInstance().getScheduler().runTask(legendaryGuild,()->menuPanel.open());
                        return;
                    }
                    if (user.isChat()) {
                        if (str.startsWith("/")) {
                            return;
                        }
                        Guild guild = UserAPI.getGuild(user.getPlayer()).orElse(null);
                        Position position = legendaryGuild.getPositionsManager().getPosition(user.getPosition()).orElse(legendaryGuild.getPositionsManager().getDefaultPosition());
                        if (guild != null) {
                            event.setCancelled(true);
                            String deal = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(user.getPlayer()),legendaryGuild.getFileManager().getConfig().GUILD_CHAT.replace("%player%",user.getPlayer())
                                    .replace("%message%",str)
                                    .replace("%position%",position.getDisplay()));
                            legendaryGuild.getMsgUtils().sendGuildMessage(guild.getMembers(),deal);
                        }
                    }
                }
            }
        });
    }
    public class Input {
        private int id;
        private String value;

        public Input(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }
}
