package net.cozyvanilla.cozylib.utilities.time;

import net.cozyvanilla.cozylib.modules.messages.Console;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class LocalDateUtils {

    /**
     * Retrieves a {@link ZoneId} from the given timezone string.
     * Falls back to system default if the timezone is invalid.
     *
     * @param timezone the timezone ID
     * @return valid ZoneId or system default
     */
    public static ZoneId getZoneId(String timezone) {
        try {
            return ZoneId.of(timezone);
        } catch (Exception e) {
            Console.severe(timezone + " is not a valid timezone. Defaulting to system default.");
        }

        return ZoneId.systemDefault();
    }

    /**
     * Calculates the number of days between a past {@link Instant} and today based on a timezone.
     *
     * @param past      the past instant
     * @param timezone  the timezone to use
     * @return number of days since the past instant
     */
    public static long getDaysSince(Instant past, String timezone) {
        ZoneId zone = getZoneId(timezone);
        LocalDate pastDate = past.atZone(zone).toLocalDate();
        return ChronoUnit.DAYS.between(pastDate, LocalDate.now(zone));
    }
}
