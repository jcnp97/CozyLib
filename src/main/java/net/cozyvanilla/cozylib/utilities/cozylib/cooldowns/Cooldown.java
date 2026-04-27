package net.cozyvanilla.cozylib.utilities.cozylib.cooldowns;

import net.cozyvanilla.cozylib.utilities.java.PerEntryExpiryCache;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public final class Cooldown {

    /**
     * Utility class for handling named cooldowns per owner, player, and id.
     */
    private static final PerEntryExpiryCache<String, Long> EXPIRY_TIMESTAMPS =
            PerEntryExpiryCache.create(10_000, key -> Duration.ofSeconds(60));

    private Cooldown() {}

    /**
     * Builds a unique cooldown key from the owner class, player UUID, and cooldown id.
     *
     * @param owner class that owns the cooldown
     * @param uuid player UUID
     * @param id cooldown identifier
     * @return unique cache key for the cooldown
     */
    private static String keyOf(Class<?> owner, UUID uuid, String id) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(id, "id");

        return owner.getName() + ":" + uuid + ":" + id;
    }

    /**
     * Stores a cooldown entry using the given key and duration.
     *
     * @param key unique cooldown key
     * @param seconds cooldown duration in seconds
     */
    private static void add(String key, long seconds) {
        long future = System.currentTimeMillis() + (seconds * 1000L);
        EXPIRY_TIMESTAMPS.put(key, future, Duration.ofSeconds(seconds));
    }

    /**
     * Starts a cooldown for the given owner, player, and id.
     *
     * @param owner class that owns the cooldown
     * @param uuid player UUID
     * @param id cooldown identifier
     * @param seconds cooldown duration in seconds
     * @throws IllegalArgumentException if seconds is not greater than 0
     */
    public static void start(Class<?> owner, UUID uuid, String id, long seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be greater than 0");
        }

        add(keyOf(owner, uuid, id), seconds);
    }

    /**
     * Starts a cooldown only if one is not already active.
     *
     * @param owner class that owns the cooldown
     * @param uuid player UUID
     * @param id cooldown identifier
     * @param seconds cooldown duration in seconds
     * @return true if the cooldown was started, false if already active or invalid
     */
    public static boolean tryStart(Class<?> owner, UUID uuid, String id, long seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be greater than 0");
        }

        String key = keyOf(owner, uuid, id);
        Long expiresAt = EXPIRY_TIMESTAMPS.getIfPresent(key);
        if (expiresAt == null) {
            start(owner, uuid, id, seconds);
            return true;
        }

        return false;
    }

    /**
     * Gets the remaining cooldown time in seconds.
     *
     * @param owner class that owns the cooldown
     * @param uuid player UUID
     * @param id cooldown identifier
     * @return remaining time in seconds, or 0 if no cooldown exists
     */
    public static long get(Class<?> owner, UUID uuid, String id) {
        Long expiresAt = EXPIRY_TIMESTAMPS.getIfPresent(keyOf(owner, uuid, id));
        return expiresAt == null ? 0L : ((expiresAt - System.currentTimeMillis()) / 1000);
    }

    /**
     * Gets the remaining cooldown time in seconds, or starts a new cooldown if none is active.
     *
     * @param owner class that owns the cooldown
     * @param uuid player UUID
     * @param id cooldown identifier
     * @param seconds cooldown duration in seconds
     * @return remaining seconds, 0 if a new cooldown was started
     */
    public static long getOrStart(Class<?> owner, UUID uuid, String id, long seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be greater than 0");
        }

        String key = keyOf(owner, uuid, id);
        Long expiresAt = EXPIRY_TIMESTAMPS.getIfPresent(key);
        if (expiresAt != null) {
            return get(owner, uuid, id);
        }

        start(owner, uuid, id, seconds);
        return 0;
    }
}