package net.cozyvanilla.cozylib.utilities.time;

import net.cozyvanilla.cozylib.Enums;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InstantUtils {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MMMM dd, yyyy | h:mm a", Locale.ENGLISH);

    /**
     * Gets the current timestamp as an {@link Instant}.
     */
    public static Instant now() { return Instant.now(); }

    /**
     * Returns a future {@link Instant} by adding the given seconds to the current time.
     *
     * @param seconds amount of seconds to add
     */
    public static Instant future(long seconds) {
        return Instant.now().plusSeconds(seconds);
    }

    /**
     * Returns a future {@link Instant} by adding a value based on the specified time unit.
     *
     * @param value amount of time units to add
     * @param unit  unit of time (SECOND, MINUTE, HOUR)
     */
    public static Instant future(long value, Enums.TimeUnits unit) {
        long added = 0L;

        switch (unit) {
            case SECOND -> added = 1L;
            case MINUTE -> added = 60L;
            case HOUR -> added = 3600L;
        }

        return Instant.now().plusSeconds(added * value);
    }

    /**
     * Checks if the given {@link Instant} has already passed.
     *
     * @param time the time to check
     * @return true if expired, false otherwise
     */
    public static boolean hasExpired(Instant time) {
        return Instant.now().isAfter(time);
    }

    /**
     * Checks if the given {@link Instant} is in the future.
     *
     * @param time the time to check
     * @return true if in the future, false otherwise
     */
    public static boolean isFuture(Instant time) { return Instant.now().isBefore(time); }

    /**
     * Converts an {@link Instant} to its ISO-8601 string representation.
     *
     * @param time the instant to convert
     * @return formatted string
     */
    @Nullable
    public static String toString(Instant time) {
        if (time == null) { return null;}
        return DateTimeFormatter.ISO_INSTANT.format(time);
    }

    /**
     * Parses an ISO-8601 string into an {@link Instant}.
     *
     * @param time the string to parse
     * @return parsed instant
     */
    @Nullable
    public static Instant toInstant(String time) {
        if (time == null) { return null;}
        return Instant.parse(time);
    }

    /**
     * Converts an {@link Instant} into a formatted readable string using the given time zone.
     *
     * @param time the Instant to format
     * @param timeZone the time zone ID (e.g., "America/New_York")
     * @return formatted date-time string, or null if input is invalid or timezone is invalid
     */
    @Nullable
    public static String toReadable(Instant time, String timeZone) {
        if (time == null || timeZone == null || timeZone.isEmpty()) {
            return null;
        }

        try {
            ZoneId zoneId = ZoneId.of(timeZone);
            return FORMATTER.format(time.atZone(zoneId));
        } catch (Exception e) {
            // invalid timezone
            return null;
        }
    }
}