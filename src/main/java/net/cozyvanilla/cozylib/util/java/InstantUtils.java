package net.cozyvanilla.cozylib.util.java;

import net.cozyvanilla.cozylib.common.enums.DateFormat;
import net.cozyvanilla.cozylib.common.enums.TimeUnit;

import javax.annotation.Nullable;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class InstantUtils {

    private InstantUtils() {}

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
    public static Instant future(long value, TimeUnit unit) {
        long multiplier = 0L;

        switch (unit) {
            case SECOND -> multiplier = 1L;
            case MINUTE -> multiplier = 60L;
            case HOUR -> multiplier = 3600L;
        }

        return future(multiplier * value);
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
     * Converts an {@link Instant} into a formatted readable date-time string using the given format and timezone.
     *
     * @param time the instant to format
     * @param dateFormat the predefined date format enum
     * @param timeZone the timezone ID (e.g., "UTC", "America/New_York")
     * @return formatted date-time string, or null if input is invalid or formatting fails
     */
    @Nullable
    public static String toReadable(Instant time, DateFormat dateFormat, String timeZone) {
        if (time == null || dateFormat == null || timeZone == null || timeZone.isEmpty()) {
            return null;
        }
        try {
            ZoneId zoneId = ZoneId.of(timeZone);
            DateTimeFormatter formatter = dateFormat.getFormatter();

            return formatter.format(time.atZone(zoneId));
        } catch (Exception e) {
            // invalid timezone or formatting issue
            return null;
        }
    }

    /**
     * Converts an {@link Instant} into a readable date-time string using default format and system timezone.
     *
     * @param time the instant to format
     * @return formatted date-time string, or null if input is invalid
     */
    @Nullable
    public static String toReadable(Instant time) {
        return toReadable(
                time,
                DateFormat.FULL_DATETIME,
                ZoneId.systemDefault().getId()
        );
    }

    /**
     * Converts an {@link Instant} into a readable date-time string using default format and specified timezone.
     *
     * @param time the instant to format
     * @param timeZone the timezone ID
     * @return formatted date-time string, or null if input is invalid
     */
    @Nullable
    public static String toReadable(Instant time, String timeZone) {
        return toReadable(
                time,
                DateFormat.FULL_DATETIME,
                timeZone
        );
    }

    /**
     * Converts an {@link Instant} into a readable date-time string using specified format and system timezone.
     *
     * @param time the instant to format
     * @param dateFormat the predefined date format enum
     * @return formatted date-time string, or null if input is invalid
     */
    @Nullable
    public static String toReadable(Instant time, DateFormat dateFormat) {
        return toReadable(
                time,
                dateFormat,
                ZoneId.systemDefault().getId()
        );
    }

    /**
     * Parses a readable date-time string into an {@link Instant} using the given format and timezone.
     *
     * @param time the string to parse
     * @param dateFormat the predefined date format enum
     * @param timeZone the timezone ID used for parsing (required for non-zoned formats)
     * @return parsed instant, or null if input is invalid or parsing fails
     */
    @Nullable
    public static Instant fromReadable(String time, DateFormat dateFormat, String timeZone) {
        if (time == null || dateFormat == null || time.isEmpty()) {
            return null;
        }

        try {
            DateTimeFormatter formatter = dateFormat.getFormatter();

            switch (dateFormat) {
                case FULL_DATETIME_ZONED:
                    return ZonedDateTime.parse(time, formatter).toInstant();

                case FULL_DATE:
                case ISO_DATE: {
                    ZoneId zoneId = ZoneId.of(timeZone);
                    LocalDate date = LocalDate.parse(time, formatter);
                    return date.atStartOfDay(zoneId).toInstant();
                }

                case FULL_DATETIME:
                case ISO_DATETIME: {
                    ZoneId zoneId = ZoneId.of(timeZone);
                    LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                    return dateTime.atZone(zoneId).toInstant();
                }

                default:
                    return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses a readable date-time string into an {@link Instant} using the given format and system timezone.
     *
     * @param time the string to parse
     * @param dateFormat the predefined date format enum
     * @return parsed instant, or null if input is invalid or parsing fails
     */
    @Nullable
    public static Instant fromReadable(String time, DateFormat dateFormat) {
        return fromReadable(time, dateFormat, ZoneId.systemDefault().getId());
    }

    /**
     * Calculates how much time has passed since the given {@link Instant}
     * in the specified {@link TimeUnit}.
     *
     * <p>Examples:
     * <ul>
     *     <li>If unit = DAY and time was 3 days ago → returns 3</li>
     *     <li>If unit = WEEK and time was 6.5 weeks ago → returns 6</li>
     *     <li>If unit = WEEK and time was less than 1 week ago → returns 0</li>
     *     <li>If time is in the future → returns 0</li>
     * </ul>
     *
     * @param time the past instant to compare against current time
     * @param unit the unit to measure the difference in
     * @return elapsed time in whole units, or 0 if negative or less than one unit
     */
    public static long timeSince(Instant time, TimeUnit unit) {
        if (time == null || unit == null) {
            return 0L;
        }

        long nowMillis = Instant.now().toEpochMilli();
        long timeMillis = time.toEpochMilli();

        return diffFloor(nowMillis - timeMillis, unit);
    }

    /**
     * Calculates the exact time elapsed since the given {@link Instant}
     * in the specified {@link TimeUnit}, returning a fractional value.
     *
     * <p>Examples:
     * <ul>
     *     <li>If unit = WEEK and time was 6.5 weeks ago → returns ~6.5</li>
     *     <li>If unit = DAY and time was 3.25 days ago → returns ~3.25</li>
     *     <li>If time is in the future → returns 0.0</li>
     * </ul>
     *
     * @param time the past instant to compare against current time
     * @param unit the unit to measure the difference in
     * @return elapsed time in units (fractional), or 0.0 if negative
     */
    public static double timeSinceExact(Instant time, TimeUnit unit) {
        if (time == null || unit == null) {
            return 0.0;
        }

        long nowMillis = Instant.now().toEpochMilli();
        long timeMillis = time.toEpochMilli();

        return diffExact(nowMillis - timeMillis, unit);
    }

    /**
     * Calculates how much time remains before the given {@link Instant}
     * in the specified {@link TimeUnit}.
     *
     * <p>Examples:
     * <ul>
     *     <li>If unit = DAY and time is 3 days in the future → returns 3</li>
     *     <li>If unit = WEEK and time is 6.5 weeks in the future → returns 6</li>
     *     <li>If unit = WEEK and time is less than 1 week away → returns 0</li>
     *     <li>If time is in the past → returns 0</li>
     * </ul>
     *
     * @param time the future instant to compare against current time
     * @param unit the unit to measure the difference in
     * @return remaining time in whole units, or 0 if negative or less than one unit
     */
    public static long timeUntil(Instant time, TimeUnit unit) {
        if (time == null || unit == null) {
            return 0L;
        }

        long nowMillis = Instant.now().toEpochMilli();
        long timeMillis = time.toEpochMilli();

        return diffFloor(timeMillis - nowMillis, unit);
    }

    /**
     * Calculates the exact time remaining before the given {@link Instant}
     * in the specified {@link TimeUnit}, returning a fractional value.
     *
     * <p>Examples:
     * <ul>
     *     <li>If unit = WEEK and time is 6.5 weeks ahead → returns ~6.5</li>
     *     <li>If unit = DAY and time is 2.75 days ahead → returns ~2.75</li>
     *     <li>If time is in the past → returns 0.0</li>
     * </ul>
     *
     * @param time the future instant to compare against current time
     * @param unit the unit to measure the difference in
     * @return remaining time in units (fractional), or 0.0 if negative
     */
    public static double timeUntilExact(Instant time, TimeUnit unit) {
        if (time == null || unit == null) {
            return 0.0;
        }

        long nowMillis = Instant.now().toEpochMilli();
        long timeMillis = time.toEpochMilli();

        return diffExact(timeMillis - nowMillis, unit);
    }

    // ------------ private helpers ------------
    private static long diffFloor(long diffMillis, TimeUnit unit) {
        if (diffMillis <= 0) return 0L;
        return diffMillis / unit.toMillis(1);
    }

    private static double diffExact(long diffMillis, TimeUnit unit) {
        if (diffMillis <= 0) return 0.0;
        return (double) diffMillis / unit.toMillis(1);
    }
}