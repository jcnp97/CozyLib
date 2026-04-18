package net.cozyvanilla.cozylib.utilities.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");

    /**
     * Converts a {@link Date} into a formatted string using the system default time zone.
     *
     * @param date the date to format
     * @return the formatted date string, or null if the date is null
     */
    public static String toString(Date date) {
        if (date == null) return null;

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(FORMATTER);
    }

    /**
     * Parses a formatted date string into a {@link Date} using the system default time zone.
     *
     * @param date the formatted date string
     * @return the parsed date, or null if the string is null or empty
     */
    public static Date toDate(String date) {
        if (date == null || date.isEmpty()) return null;

        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);

        return Date.from(
                localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    /**
     * Converts a {@link java.sql.Date} into a {@link Date}.
     *
     * @param sqlDate the SQL date to convert
     * @return the converted util date, or null if the SQL date is null
     */
    public static Date sqlToUtil(java.sql.Date sqlDate) {
        return (sqlDate == null) ? null : new Date(sqlDate.getTime());
    }

    /**
     * Converts a {@link Date} into a {@link java.sql.Date}.
     *
     * @param utilDate the util date to convert
     * @return the converted SQL date, or null if the util date is null
     */
    public static java.sql.Date utilToSql(Date utilDate) {
        return (utilDate == null) ? null : new java.sql.Date(utilDate.getTime());
    }
}
