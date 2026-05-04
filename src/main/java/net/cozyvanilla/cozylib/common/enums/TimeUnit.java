package net.cozyvanilla.cozylib.common.enums;

// Example usage:
// TimeUnit.DAY.toMillis(3): 3 days → 259,200,000 ms
// TimeUnit.DAY.toTicks(3) → 5,184,000 ticks
// TimeUnit.WEEK.toTicks(1.5): 1.5 weeks → ticks (ceil applied)
public enum TimeUnit {
    WEEK(604_800_000L),
    DAY(86_400_000L),
    HOUR(3_600_000L),
    MINUTE(60_000L),
    SECOND(1_000L),
    MILLISECOND(1L);

    private final long millis;

    TimeUnit(long millis) {
        this.millis = millis;
    }

    public long toMillis(long value) {
        return value * millis;
    }

    public long toTicks(long value) {
        long millis = toMillis(value);
        return Math.ceilDiv(millis, 50L);
    }

    public long toTicks(double value) {
        long millis = (long) Math.ceil(value * this.millis);
        return Math.ceilDiv(millis, 50L);
    }

    public long toSeconds(long value) {
        long millis = toMillis(value);
        return Math.ceilDiv(millis, 1_000L);
    }

    public long toSeconds(double value) {
        long millis = (long) Math.ceil(value * this.millis);
        return Math.ceilDiv(millis, 1_000L);
    }
}