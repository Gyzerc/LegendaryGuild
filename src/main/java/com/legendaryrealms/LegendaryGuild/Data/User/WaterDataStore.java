package com.legendaryrealms.LegendaryGuild.Data.User;

import java.util.HashMap;
import java.util.Optional;

public class WaterDataStore {

    private HashMap<String,WaterData> data;
    public WaterDataStore(HashMap<String,WaterData> data) {
        this.data = data;
    }
    public Optional<WaterData> getWaterData(String id){
        return data.containsKey(id) ? Optional.of(data.get(id)) : Optional.empty();
    }
    public void clearWaterDay(){
        for (String id : data.keySet()){
            WaterData waterData = data.get(id);
            waterData.setAmount(WaterDataType.TODAY,0);
            data.put(id,waterData);
        }
    }

    public void clearWater(String id) {
        if (data.containsKey(id)) {
            WaterData waterData = data.get(id);
            waterData.setAmount(WaterDataType.TODAY,0);
            data.put(id,waterData);
        }
    }
    public String toString_Day(){
        StringBuilder builder = new StringBuilder();
        for (String id : data.keySet()){
            WaterData waterData = data.get(id);
            builder.append(waterData.toString_Day()).append(";");
        }
        return builder.toString();
    }
    public String toString_Total(){
        StringBuilder builder = new StringBuilder();
        for (String id : data.keySet()){
            WaterData waterData = data.get(id);
            builder.append(waterData.toString_Total()).append(";");
        }
        return builder.toString();
    }

    public int getAmount(String id,WaterDataType type){
        if (data.containsKey(id)) {
            return data.get(id).getAmount(type);
        }
        return 0;
    }

    public void addAmount(String id,WaterDataType type,int amount){
        WaterData data = this.data.get(id);
        if (data == null){
            data = new WaterData(id,0,0);
            this.data.put(id,data);
        }
        data.addAmount(type,amount);
    }

    public void takeAmount(String id,WaterDataType type,int amount){
        WaterData data = this.data.get(id);
        if (data == null){
            return;
        }
        data.takeAmount(type,amount);
    }

    public static class WaterData {
        private String id;
        private int today;
        private int total;

        public WaterData(String id, int today, int total) {
            this.id = id;
            this.today = today;
            this.total = total;
        }

        public String toString_Day(){
            StringBuilder builder=new StringBuilder();
            return builder.append(id).append(",").append(today).toString();
        }
        public String toString_Total(){
            StringBuilder builder=new StringBuilder();
            return builder.append(id).append(",").append(total).toString();
        }

        public int getAmount(WaterDataType type){
            switch (type){
                case TODAY:
                    return today;
                case TOTAL:
                    return total;
                default:
                    return 0;
            }
        }

        public void addAmount(WaterDataType type,int amount){
            switch (type){
                case TODAY:
                    today += amount;
                    return;
                case TOTAL:
                    total += amount;
                    return;
            }
        }

        public void takeAmount(WaterDataType type,int amount){
            switch (type){
                case TODAY:
                    today = today - amount > 0 ? today - amount : 0;
                    return;
                case TOTAL:
                    total = total - amount > 0 ? total - amount : 0;
                    return;
            }
        }

        public void setAmount(WaterDataType type,int amount){
            switch (type){
                case TODAY:
                    today = amount;
                    return;
                case TOTAL:
                    total = amount;
                    return;
            }
        }
    }

    public enum WaterDataType {
        TODAY,
        TOTAL;
    }
}
