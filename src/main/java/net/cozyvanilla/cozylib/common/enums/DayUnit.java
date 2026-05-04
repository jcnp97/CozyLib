package net.cozyvanilla.cozylib.common.enums;

public enum DayUnit {
    HOURLY(3600),
    DAILY(86400),
    WEEKLY(604800),
    MONTHLY(2592000);

    private final long seconds;
    DayUnit(long seconds) {
        this.seconds = seconds;
    }
    public long getSeconds() {
        return seconds;
    }
}