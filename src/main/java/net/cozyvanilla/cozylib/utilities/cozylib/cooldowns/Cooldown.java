package net.cozyvanilla.cozylib.utilities.cozylib.cooldowns;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Cooldown {
    private static final Map<String, Long> EXPIRY_TIMESTAMPS = new ConcurrentHashMap<>();

    private Cooldown() {}

    private static String key(Class<?> owner, UUID uuid, String id) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(id, "id");

        return owner.getName() + ":" + uuid + ":" + id;
    }

    public static void add(Class<?> owner, UUID uuid, String id, long seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be greater than 0");
        }

        long expiresAt = System.currentTimeMillis() + (seconds * 1000L);
        EXPIRY_TIMESTAMPS.put(key(owner, uuid, id), expiresAt);
    }

    public static boolean tryAdd(Class<?> owner, UUID uuid, String id, long seconds) {
        String token = key(owner, uuid, id);
        long now = System.currentTimeMillis();
        long expiresAt = EXPIRY_TIMESTAMPS.getOrDefault(token, 0L);

        if (expiresAt > now) {
            return false;
        }

        EXPIRY_TIMESTAMPS.put(token, now + (seconds * 1000L));
        return true;
    }

    public static long get(Class<?> owner, UUID uuid, String id) {
        String token = key(owner, uuid, id);
        long expiresAt = EXPIRY_TIMESTAMPS.getOrDefault(token, 0L);
        long remainingMillis = expiresAt - System.currentTimeMillis();

        if (remainingMillis <= 0) {
            EXPIRY_TIMESTAMPS.remove(token);
            return 0;
        }

        return Math.max(1, (remainingMillis + 999) / 1000);
    }

    public static void clear(Class<?> owner, UUID uuid, String id) {
        EXPIRY_TIMESTAMPS.remove(key(owner, uuid, id));
    }
}