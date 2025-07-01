package org.xijinping.bot.time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerManager {
    List<Timer> timers = new ArrayList<>();

    ScheduledExecutorService executor;

    public TimerManager() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::updateTimers, 0, 1, TimeUnit.SECONDS);
    }

    public void updateTimers() {
        List<Timer> toRemove = new ArrayList<>();
        timers.forEach(t -> {
            t.updateMessage();

            if(!t.active) {
                toRemove.add(t);
            }
        });

        toRemove.forEach(t -> timers.remove(t));
    }

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    public List<Timer> getTimers() {
        return timers;
    }
}
