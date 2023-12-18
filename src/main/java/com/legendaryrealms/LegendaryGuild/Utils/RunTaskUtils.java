package com.legendaryrealms.LegendaryGuild.Utils;

import com.legendaryrealms.LegendaryGuild.LegendaryGuild;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class RunTaskUtils extends BukkitRunnable {

    private int periodTick;
    private int times;
    private int sec = 0;
    private Player p;
    private Consumer<RunTaskUtils> consumerEvery = null;
    private Consumer<RunTaskUtils> consumerEnd = null;

    public RunTaskUtils(int periodTick,int times,Player p){
        this.periodTick = periodTick;
        this.times = times;
        this.p = p;
    }

    public RunTaskUtils setTaskEveryPeriod(Consumer<RunTaskUtils> consumer){
        this.consumerEvery = consumer;
        return this;
    }

    public RunTaskUtils setConsumerEnd(Consumer<RunTaskUtils> consumerEnd) {
        this.consumerEnd = consumerEnd;
        return this;
    }

    @Override
    public void run() {
        if (sec == times){
            if (consumerEnd != null) {
                consumerEnd.accept(this);
            }
            cancel();
            return;
        }
        consumerEvery.accept(this);
        sec++;
    }

    public RunTaskUtils start(){
        if (consumerEvery == null) {
            return this;
        }
        this.runTaskTimerAsynchronously(LegendaryGuild.getInstance(),0,20);
        return this;
    }

    public int getPeriodTick() {
        return periodTick;
    }

    public int getTimes() {
        return times;
    }

    public int getSec() {
        return sec;
    }

    public Player getPlayer() {
        return p;
    }
}
