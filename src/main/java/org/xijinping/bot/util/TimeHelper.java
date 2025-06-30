package org.xijinping.bot.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

    public static String getTimeDifferenceAsString(Date start, Date end) {
        long diffMillis = end.getTime() - start.getTime();
    
        return getTimeAsString(diffMillis);
    }
    
    public static String getTimeAsString(long timeInMilliseconds) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMilliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) % 60;

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
