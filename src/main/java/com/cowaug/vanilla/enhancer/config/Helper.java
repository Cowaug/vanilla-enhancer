package com.cowaug.vanilla.enhancer.config;

import java.util.concurrent.TimeUnit;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastFrom(Object object) {
        return (T) object;
    }

    public static String TicksToTime(long tick, boolean shortForm) {
        tick = tick / 20;
        int days = (int) TimeUnit.SECONDS.toDays(tick);
        int hours = (int) (TimeUnit.SECONDS.toHours(tick) - TimeUnit.DAYS.toHours(days));
        int minutes = (int) (TimeUnit.SECONDS.toMinutes(tick) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days));
        int seconds = (int) (TimeUnit.SECONDS.toSeconds(tick) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("d ");
            if (shortForm) {
                return result.toString().trim();
            }
        }

        if (hours > 0 || days > 0) {
            result.append(hours).append("h ");
            if (shortForm) {
                return result.toString().trim();
            }
        }

        if (minutes > 0 || hours > 0 || days > 0) {
            result.append(minutes).append("m ");
            if (shortForm) {
                return result.toString().trim();
            }
        }

        result.append(seconds).append("s");
        return result.toString();
    }
}
