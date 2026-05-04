package net.cozyvanilla.cozylib.common.enums;

import java.time.format.DateTimeFormatter;

public enum DateFormat {
    FULL_DATE("MMMM d, yyyy"), // April 30, 2026
    ISO_DATE("yyyy-MM-dd"), // 2026-04-30
    FULL_DATETIME("MMMM d, yyyy (h:mm a)"), // April 30, 2026 (3:00 PM)
    ISO_DATETIME("yyyy-MM-dd HH:mm"), // 2026-04-30 15:00
    FULL_DATETIME_ZONED("MMMM d, yyyy (h:mm a/'GMT'X)"); // April 30, 2026 (3:00 PM/GMT+10)

    private final String pattern;

    DateFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(pattern);
    }
}
