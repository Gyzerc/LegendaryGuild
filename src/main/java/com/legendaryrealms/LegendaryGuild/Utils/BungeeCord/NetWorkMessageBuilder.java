package com.legendaryrealms.LegendaryGuild.Utils.BungeeCord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetWorkMessageBuilder {



    private String reciver;
    private MessageType messageType;
    private NetWorkMessage netWorkMessage;
    public NetWorkMessageBuilder setReciver(String reciver){
        this.reciver = reciver;
        return this;
    }
    public NetWorkMessageBuilder setMessageType(MessageType messageType){
        this.messageType = messageType;
        return this;
    }
    public NetWorkMessageBuilder setNetWorkMessage(NetWorkMessage netWorkMessage){
        this.netWorkMessage = netWorkMessage;
        return this;
    }
    public void sendPluginMessage(Player p){
        if (LegendaryGuild.getInstance().getNetWork().isEnable()) {
            if (this.reciver != null && this.messageType != null && this.netWorkMessage != null && p!=null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(messageType.name()); // So BungeeCord knows to forward it
                out.writeUTF(reciver);
                out.writeUTF("legendaryguild"); // The channel name to check if this your data

                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                try {
                    msgout.writeUTF(netWorkMessage.toString()); // You can do anything you want with msgout
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                p.sendPluginMessage(LegendaryGuild.getInstance(), "BungeeCord", out.toByteArray());

            }
        }
    }



    public enum MessageType {
        Forward,
        PlayerList,
        ForwardToPlayer
    }
}
