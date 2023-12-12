package com.legendaryrealms.LegendaryGuild.Utils.BungeeCord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class NetWork {
    public LegendaryGuild legendaryGuild;
    private boolean enable;
    public NetWork(LegendaryGuild legendaryGuild){
        this.legendaryGuild = legendaryGuild;
        initNetwork();
    }

    public abstract void initNetwork();
    public abstract void disable();

    public void recive(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")){
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        //获取全服玩家数量
        if (subchannel.equals("PlayerList")){
            String server = in.readUTF();
            String[] playerList = in.readUTF().split(", ");
            LegendaryGuild.setPlayers(Arrays.stream(playerList).collect(Collectors.toList()));
            return;
        }
        if (!subchannel.equals("legendaryguild")){
            return;
        }

        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);
        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        try {
            String data = msgin.readUTF();
            String[] args=data.split(";");
            NetWorkMessage.NetWorkType type= NetWorkMessage.NetWorkType.valueOf(args[0]);
            String target = args[1];

            //执行
            handle(type,target);
        } catch (IOException e) {
            legendaryGuild.info("信息读取出错！",Level.SEVERE,e);
        } catch (IllegalArgumentException e){
            legendaryGuild.info("信息接受转化出错！",Level.SEVERE,e);
        }
    }

    public abstract void handle(NetWorkMessage.NetWorkType type,String value);

    public void teleportServer(Player p,String target){
        ByteArrayDataOutput outputStream = ByteStreams.newDataOutput();
        outputStream.writeUTF("Connect");
        outputStream.writeUTF(target);
        p.sendPluginMessage(legendaryGuild,"BungeeCord",outputStream.toByteArray());
    }

    public void sendMessage(Player sender,String target,String msg){
        ByteArrayDataOutput outputStream = ByteStreams.newDataOutput();
        outputStream.writeUTF("Message");
        outputStream.writeUTF(target);
        outputStream.writeUTF(msg);
        sender.sendPluginMessage(legendaryGuild,"BungeeCord",outputStream.toByteArray());
    }
    public void sendALLMessage(Player sender,String msg){
        ByteArrayDataOutput outputStream = ByteStreams.newDataOutput();
        outputStream.writeUTF("Message");
        outputStream.writeUTF("ALL");
        outputStream.writeUTF(msg);
        sender.sendPluginMessage(legendaryGuild,"BungeeCord",outputStream.toByteArray());
    }


    public void PlayerList(Player sender){
        ByteArrayDataOutput outputStream = ByteStreams.newDataOutput();
        outputStream.writeUTF("PlayerList");
        outputStream.writeUTF("ALL");
        sender.sendPluginMessage(legendaryGuild,"BungeeCord",outputStream.toByteArray());
    }
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
