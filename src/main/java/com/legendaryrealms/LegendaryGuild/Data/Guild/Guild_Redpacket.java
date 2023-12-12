package com.legendaryrealms.LegendaryGuild.Data.Guild;

import java.util.HashMap;
import java.util.UUID;

public class Guild_Redpacket {
    private String guild;
    private HashMap<UUID,Redpacket> redpackets;

    public Guild_Redpacket(String guild, HashMap<UUID,Redpacket> redpackets) {
        this.guild = guild;
        this.redpackets = redpackets;
    }

    public String getGuild() {
        return guild;
    }

    public HashMap<UUID,Redpacket> getRedpackets() {
        return redpackets;
    }

    public void setRedpackets(HashMap<UUID,Redpacket> redpackets) {
        this.redpackets = redpackets;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        if (redpackets.isEmpty()){
            return "";
        }
        redpackets.forEach((uid,redpacket) -> {
            builder.append(redpacket.getUuid()).append("⁝").append(redpacket.getPlayer()).append("⁝").append(redpacket.getDate()).append("⁝")
                    .append(redpacket.getTotal()).append("⁝").append(redpacket.getAmount()).append("⁝").append(redpacket.getLess()).append("⁝")
                    .append(redpacket.historytoString()).append("⁞");
        });
        return builder.toString();
    }

    public static Guild_Redpacket toData(String guild,String str){
        if (str == null){
            return new Guild_Redpacket(guild,new HashMap<>());
        }
        if (str.isEmpty()){
            return new Guild_Redpacket(guild,new HashMap<>());
        }

        HashMap<UUID,Redpacket> redpackets = new HashMap<>();
        for (String data : str.split("⁞")){
            String args[] = data.split("⁝");
            UUID uuid = UUID.fromString(args[0]);
            String player = args[1];
            String date = args[2];
            double total = Double.parseDouble(args[3]);
            int amount = Integer.parseInt(args[4]);
            double less = Double.parseDouble(args[5]);
            if (args.length > 6) {
                HashMap<String,Double> map = Redpacket.toHistory(args[6]);
                redpackets.put(uuid, new Redpacket(uuid, date, player, total, less, amount, map));
            }
            else {
                redpackets.put(uuid, new Redpacket(uuid, date, player, total, less, amount, new HashMap<>()));
            }
        }
        return new Guild_Redpacket(guild,redpackets);
    }

    public static class Redpacket {
        private UUID uuid;
        private String date;
        private String player;
        private double total;
        private double less;
        private int amount;
        private HashMap<String,Double> history;

        public Redpacket(UUID uuid, String date, String player, double total, double less, int amount, HashMap<String, Double> history) {
            this.uuid = uuid;
            this.date = date;
            this.player = player;
            this.total = total;
            this.less = less;
            this.amount = amount;
            this.history = history;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public void setLess(double less) {
            this.less = less;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public void setHistory(HashMap<String, Double> history) {
            this.history = history;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getDate() {
            return date;
        }

        public String getPlayer() {
            return player;
        }

        public double getTotal() {
            return total;
        }


        public double getLess() {
            return less;
        }

        public int getAmount() {
            return amount;
        }

        public HashMap<String, Double> getHistory() {
            return history;
        }

        public String historytoString(){
            if (history.size() <= 0){
                return "";
            }
            StringBuilder builder = new StringBuilder();
            history.forEach(((p,a) -> {
                builder.append(p+"="+a).append(",");
            }));
            return builder.toString();
        }
        public static HashMap<String,Double> toHistory(String str){
            if (str == null) {return new HashMap<>();}
            if (str.isEmpty()) {return new HashMap<>();}
            HashMap<String,Double> map = new HashMap<>();
            for (String data : str.split(",")){
                String[] args=data.split("=");
                map.put(args[0],Double.parseDouble(args[1]));
            }
            return map;
        }
    }


    public enum Packet_Curreny {
        Vault,
        PlayerPoints;
    }
}
